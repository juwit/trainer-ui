package com.miage.altea.tp.pokemon_ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

    @Value("${trainer.service.username}")
    String username;

    @Value("${trainer.service.password}")
    String password;

    @Bean
    RestTemplate trainerApiRestTemplate(){
        var restTemplate = new RestTemplate();
        var basicAuthInterceptor = new BasicAuthenticationInterceptor(username, password);
        restTemplate.getInterceptors().add(basicAuthInterceptor);
        return restTemplate;
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
