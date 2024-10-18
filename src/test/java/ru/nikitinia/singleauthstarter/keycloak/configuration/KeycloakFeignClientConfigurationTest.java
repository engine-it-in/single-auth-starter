package ru.nikitinia.singleauthstarter.keycloak.configuration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeycloakFeignClientConfigurationTest {

    private final KeycloakFeignClientConfiguration configuration =
            new KeycloakFeignClientConfiguration();

    @Test
    void checkKeycloakFeignClientConfiguration() {
        assertThat(configuration)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void keycloakErrorDecoder_shouldBe() {
        assertThat(configuration.keycloakErrorDecoder())
                .usingRecursiveComparison()
                .isEqualTo(new KeycloakErrorDecoder())
        ;
    }

    @Test
    void keycloakRetryer_shouldBe() {

        assertThat(configuration.retryer())
                .usingRecursiveComparison()
                .isEqualTo(new KeycloakRetryer())
        ;

    }

}