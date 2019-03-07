package com.miage.altea.tp.pokemon_ui;

import com.miage.altea.tp.pokemon_ui.pokemonTypes.service.PokemonTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PokemonUITest {

    @Autowired
    PokemonTypeService pokemonTypeService;

    @Test
    void testSyncLoading(){
        var pokemonTypesIds = List.of(1,2,3);

        var pokemonTypesSync = pokemonTypesIds.parallelStream()
                .map(pokemonTypeService::getPokemonType)
                .collect(Collectors.toList());

        assertEquals(3, pokemonTypesSync.size());
    }

    @Test
    void testAsyncLoading(){
        var pokemonTypesIds = List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

        Executor executor = Executors.newFixedThreadPool(5);

        var pokemonTypesSync = pokemonTypesIds.stream()
                .map(id -> {
                    System.out.println("Map");return id;})
                .map(id -> CompletableFuture.supplyAsync(() -> pokemonTypeService.getPokemonType(id), executor))
                .map(cf -> {
                    System.out.println(Thread.currentThread().getName());
                    return cf;
                })
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        CompletableFuture.supplyAsync(() -> {System.out.println("TEST ASYNC"); return "Hello";});

        assertEquals(15, pokemonTypesSync.size());
    }

}