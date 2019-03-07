package com.miage.altea.tp.pokemon_ui.config;

import com.miage.altea.tp.pokemon_ui.pokemonTypes.bo.PokemonType;
import com.miage.altea.tp.pokemon_ui.pokemonTypes.service.PokemonTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Disabled
class RetryConfigurationTest {

    @Autowired
    PokemonTypeServiceImpl pokemonTypeService;

    RestTemplate restTemplate;

    @Value("${pokemonType.service.url}/pokemon-types/{id}")
    String expectedUrl;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        pokemonTypeService.setRestTemplate(restTemplate);
    }

    @Test
    @DirtiesContext
    void getPokemonType_shouldRetry() {
        var pikachu = new PokemonType();
        pikachu.setId(25);
        pikachu.setName("Pikachu");
        // making the mock throwing 2 exceptions, then returning a value
        when(restTemplate.getForObject(expectedUrl, PokemonType.class, 25))
                .thenThrow(new RestClientException("500"))
                .thenThrow(new RestClientException("500"))
                .thenReturn(pikachu);

        var pokemonType = pokemonTypeService.getPokemonType(25);

        // rest template should have been called thrice
        verify(restTemplate, times(3)).getForObject(expectedUrl, PokemonType.class, 25);

        assertEquals("Pikachu", pokemonType.getName());
    }

    @Test
    @DirtiesContext
    void getPokemonType_shouldRetry_andThrow() {
        // making the mock alwways throwing exceptions
        when(restTemplate.getForObject(expectedUrl, PokemonType.class, 25))
                .thenThrow(new RestClientException("500"));

        assertThrows(RestClientException.class, () -> pokemonTypeService.getPokemonType(25));

        // rest template should have been called thrice
        verify(restTemplate, times(3)).getForObject(expectedUrl, PokemonType.class, 25);
    }

}