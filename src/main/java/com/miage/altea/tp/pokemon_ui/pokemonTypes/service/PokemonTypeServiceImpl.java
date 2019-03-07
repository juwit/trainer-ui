package com.miage.altea.tp.pokemon_ui.pokemonTypes.service;

import com.miage.altea.tp.pokemon_ui.pokemonTypes.bo.PokemonType;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class PokemonTypeServiceImpl implements PokemonTypeService{

    private RestTemplate restTemplate;
    private String pokemonTypeServiceUrl;
    private CircuitBreaker circuitBreaker;
    private Retry retry;

    @Override
    @Cacheable("pokemon-types")
    public List<PokemonType> listPokemonsTypes() {
        var pokemonTypes = restTemplate.getForObject(pokemonTypeServiceUrl +"/pokemon-types", PokemonType[].class);
        return Arrays.asList(pokemonTypes);
    }

    @Override
    @Cacheable("pokemon-types")
//    @Retryable
    public PokemonType getPokemonType(int id) {
        return this.retry
                .executeSupplier(() -> restTemplate.getForObject(pokemonTypeServiceUrl +"/pokemon-types/{id}", PokemonType.class, id));
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${pokemonType.service.url}")
    void setPokemonTypeServiceUrl(String pokemonServiceUrl) {
        this.pokemonTypeServiceUrl = pokemonServiceUrl;
    }

    @Autowired
    public void setCircuitBreaker(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @Autowired
    public void setRetry(Retry retry) {
        this.retry = retry;
    }
}
