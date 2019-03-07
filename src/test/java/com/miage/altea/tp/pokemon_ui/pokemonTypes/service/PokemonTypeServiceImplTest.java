package com.miage.altea.tp.pokemon_ui.pokemonTypes.service;

import com.miage.altea.tp.pokemon_ui.pokemonTypes.bo.PokemonType;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerOpenException;
import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonTypeServiceImplTest {

    private PokemonTypeServiceImpl pokemonTypeService;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        pokemonTypeService = new PokemonTypeServiceImpl();
        pokemonTypeService.setRestTemplate(restTemplate);

        var url = "http://localhost:8080";
        pokemonTypeService.setPokemonTypeServiceUrl(url);
    }

    @Test
    void listPokemonsTypes_shouldCallTheRemoteService() {
        var pikachu = new PokemonType();
        pikachu.setName("pikachu");
        pikachu.setId(25);

        var expectedUrl = "http://localhost:8080/pokemon-types";
        when(restTemplate.getForObject(expectedUrl, PokemonType[].class)).thenReturn(new PokemonType[]{pikachu});

        var pokemons = pokemonTypeService.listPokemonsTypes();

        assertNotNull(pokemons);
        assertEquals(1, pokemons.size());

        verify(restTemplate).getForObject(expectedUrl, PokemonType[].class);
    }

    @Test
    void pokemonTypeServiceImpl_shouldBeAnnotatedWithService(){
        assertNotNull(PokemonTypeServiceImpl.class.getAnnotation(Service.class));
    }

    @Test
    void setRestTemplate_shouldBeAnnotatedWithAutowired() throws NoSuchMethodException {
        var setRestTemplateMethod = PokemonTypeServiceImpl.class.getDeclaredMethod("setRestTemplate", RestTemplate.class);
        assertNotNull(setRestTemplateMethod.getAnnotation(Autowired.class));
    }

    @Test
    void setPokemonServiceUrl_shouldBeAnnotatedWithValue() throws NoSuchMethodException {
        var setter = PokemonTypeServiceImpl.class.getDeclaredMethod("setPokemonTypeServiceUrl", String.class);
        var valueAnnotation = setter.getAnnotation(Value.class); //<1>
        assertNotNull(valueAnnotation);
        assertEquals("${pokemonType.service.url}", valueAnnotation.value()); //<2>
    }


    @Test
    void getPokemonType_shouldRetry_andThrow() {
        String expectedUrl = "http://localhost:8080/pokemon-types/{id}";

        // making the mock alwways throwing exceptions
        when(restTemplate.getForObject(expectedUrl, PokemonType.class, 25))
                .thenThrow(new RestClientException("500"));

        assertThrows(RestClientException.class, () -> pokemonTypeService.getPokemonType(25));

        // rest template should have been called thrice
        verify(restTemplate, times(3)).getForObject(expectedUrl, PokemonType.class, 25);
    }

    @Test
    void getPokemonType_shouldCircuitBreak(){
        var circuitBreaker = CircuitBreaker.of("pokemon-types",
                CircuitBreakerConfig.custom().ringBufferSizeInClosedState(2).build());

        pokemonTypeService.setCircuitBreaker(circuitBreaker);

        // the circuit should first be closed
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
        // registering 2 failures for the circuit to open
        for(int i = 0; i < 2; i++){
            circuitBreaker.onError(0, new Exception());
        }
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());

        // the call should not happen !
        assertThrows(CircuitBreakerOpenException.class, () -> pokemonTypeService.getPokemonType(25));

        // rest template should not have been called
        verifyZeroInteractions(restTemplate);
    }

}