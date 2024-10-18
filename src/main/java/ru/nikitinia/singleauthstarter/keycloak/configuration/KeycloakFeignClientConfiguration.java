package ru.nikitinia.singleauthstarter.keycloak.configuration;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;


@Configurable
public class KeycloakFeignClientConfiguration {

    @Bean
    public ErrorDecoder keycloakErrorDecoder() {
        return new KeycloakErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new KeycloakRetryer();
    }

}
