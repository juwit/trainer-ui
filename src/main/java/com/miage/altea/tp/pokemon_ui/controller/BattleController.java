package com.miage.altea.tp.pokemon_ui.controller;

import com.miage.altea.tp.pokemon_ui.trainers.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * Controller for BattleGame
 */
@Controller
@RequestMapping("/arena")
public class BattleController {

    @Autowired
    private TrainerService trainerService;

    @GetMapping("/matchmaking")
    public ModelAndView matchmaking(Principal principal){
        var modelAndView = new ModelAndView("matchmaking");

        modelAndView.addObject("trainers", trainerService.getAllTrainersExcept(principal.getName()));

        return modelAndView;
    }

    @PostMapping("/matchmaking")
    public ModelAndView matchmaking(String player1, String player2){
        System.out.println(player1);
        System.out.println(player2);
        var modelAndView = new ModelAndView("battle");

        // you play as player 1
        modelAndView.addObject("player1", trainerService.getTrainer(player1));
        modelAndView.addObject("player2", trainerService.getTrainer(player2));

        return modelAndView;
    }

}
