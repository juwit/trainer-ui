package com.miage.altea.tp.pokemon_ui.trainers.service;

import com.miage.altea.tp.pokemon_ui.pokemonTypes.bo.PokemonType;
import com.miage.altea.tp.pokemon_ui.pokemonTypes.service.PokemonTypeService;
import com.miage.altea.tp.pokemon_ui.trainers.bo.Pokemon;
import com.miage.altea.tp.pokemon_ui.trainers.bo.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService{

    private RestTemplate restTemplate;
    private String trainerServiceUrl;

    @Autowired
    private PokemonTypeService pokemonTypeService;

    @Override
    public Trainer getTrainer(String name) {
        var trainer = restTemplate.getForObject(trainerServiceUrl+"/trainers/{name}", Trainer.class, name);

        trainer.getTeam().parallelStream()
                .forEach(this::setPokemonType);

        return trainer;
    }

    private void setPokemonType(Pokemon pokemon){
        var pokemonType = pokemonTypeService.getPokemonType(pokemon.getPokemonType());
        pokemon.setType(pokemonType);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        var trainers = this.restTemplate.getForObject(trainerServiceUrl+"/trainers/", Trainer[].class);

        var trainersList = Arrays.asList(trainers);

        trainersList.parallelStream()
                .flatMap(trainer -> trainer.getTeam().stream())
                .forEach(this::setPokemonType);

        return trainersList;
    }

    @Autowired
    void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${trainer.service.url}")
    public void setTrainerServiceUrl(String trainerServiceUrl) {
        this.trainerServiceUrl = trainerServiceUrl;
    }
}
