package com.miage.altea.tp.pokemon_ui.trainers.service;


import com.miage.altea.tp.pokemon_ui.trainers.bo.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer getTrainer(String name);

    List<Trainer> getAllTrainersExcept(String trainerName);
}
