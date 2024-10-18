package ru.nikitinia.singleauthstarter.configuration.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.nikitinia.singleauthstarter.util.TestConstant.Main.TEST_STRING;

@SpringBootTest(
        classes = {
                RequestPropertiesKeycloak.class
        },
        properties = {
                "keycloak-request.grantType=string",
                "keycloak-request.clientId=string",
                "keycloak-request.clientSecret=string"
        })
@EnableConfigurationProperties(RequestPropertiesKeycloak.class)
class RequestPropertiesKeycloakTest {

    @Autowired
    private RequestPropertiesKeycloak requestPropertiesKeycloak;

    @Test
    void checkRequestPropertiesKeycloakTest() {
        assertThat(requestPropertiesKeycloak)
                .hasFieldOrPropertyWithValue("clientId", TEST_STRING)
                .hasFieldOrPropertyWithValue("grantType", TEST_STRING)
                .hasFieldOrPropertyWithValue("clientSecret", TEST_STRING)
        ;
    }

    @EnableCaching
    @Configuration
    public static class TestConfig {

        @Bean
        public CacheManager cacheManager() {
            return new CaffeineCacheManager();
        }

    }

}