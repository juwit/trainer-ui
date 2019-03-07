package com.miage.altea.tp.pokemon_ui.controller;

import com.miage.altea.tp.pokemon_ui.trainers.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class TrainerController {

    private TrainerService trainerService;

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/trainers")
    public ModelAndView trainers(Principal principal) {
        var modelAndView = new ModelAndView("trainers");
        modelAndView.addObject("trainers", trainerService.getAllTrainersExcept(principal.getName()));
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView profile(Principal principal){
        var modelAndView = new ModelAndView("profile");
        var trainer = trainerService.getTrainer(principal.getName());
        modelAndView.addObject("trainer", trainer);
        return modelAndView;
    }

}
