package com.miage.altea.tp.pokemon_ui.trainers.bo;

import java.util.List;

public class Trainer {

    private String name;

    private List<Pokemon> team;

    private String password;

    public Trainer() {
    }

    public Trainer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPassword(){
        return this.password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pokemon> getTeam() {
        return team;
    }

    public void setTeam(List<Pokemon> team) {
        this.team = team;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
