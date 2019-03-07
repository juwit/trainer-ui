package com.miage.altea.tp.pokemon_ui.trainers.bo;


import com.miage.altea.tp.pokemon_ui.pokemonTypes.bo.PokemonType;

public class Pokemon {

    private int id;

    private int pokemonType;

    private int level;

    private PokemonType type;

    public Pokemon() {
    }

    public Pokemon(int pokemonType, int level) {
        this.pokemonType = pokemonType;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(int pokemonType) {
        this.pokemonType = pokemonType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setType(PokemonType type) {
        this.type = type;
    }
}
