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

@SpringBootTest(
        properties = {
                "keycloak-connection.cachetime=1",
                "keycloak-connection.url=url",

        })
@EnableConfigurationProperties(KeycloakConnectionSettings.class)
class KeycloakConnectionSettingsTest {

    @Autowired
    private KeycloakConnectionSettings keycloakConnectionSettings;

    private final KeycloakConnectionSettings keycloakConnectionSettingsExpected =
            KeycloakConnectionSettings.builder()
                    .cachetime(1L)
                    .build();

    @Test
    void checkKeycloakConnectionSettingsTest() {
        assertThat(keycloakConnectionSettings)
                .hasFieldOrPropertyWithValue("cachetime", 1L)
                .isEqualTo(keycloakConnectionSettingsExpected)
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