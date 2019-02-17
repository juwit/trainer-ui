package com.miage.altea.tp.pokemon_ui.trainers.service;

import com.miage.altea.tp.pokemon_ui.trainers.bo.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService{

    private RestTemplate restTemplate;
    private String trainerServiceUrl;

    @Override
    public Trainer getTrainer(String name) {
        return restTemplate.getForObject(trainerServiceUrl+"/trainers/{name}", Trainer.class, name);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        var trainers = this.restTemplate.getForObject(trainerServiceUrl+"/trainers/", Trainer[].class);
        return Arrays.asList(trainers);
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
