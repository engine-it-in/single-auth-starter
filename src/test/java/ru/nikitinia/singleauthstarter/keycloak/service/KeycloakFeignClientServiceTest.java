package ru.nikitinia.singleauthstarter.keycloak.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.nikitinia.singleauthstarter.configuration.properties.RequestPropertiesKeycloak;
import ru.nikitinia.singleauthstarter.keycloak.client.KeyCloakFeignClient;
import ru.nikitinia.singleauthstarter.keycloak.model.ResponseKeycloak;
import ru.nikitinia.singleauthstarter.util.TestData;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor.BEARER;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KeycloakFeignClientService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class KeycloakFeignClientServiceTest {

    @Autowired
    private KeycloakFeignClientService keycloakFeignClientService;

    @MockBean
    private KeyCloakFeignClient keyCloakFeignClient;

    @MockBean
    private RequestPropertiesKeycloak requestPropertiesKeycloak;

    private final ByteArrayOutputStream byteArrayOutputStream =
            new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(byteArrayOutputStream));
    }


    @Test
    void checkKeycloakFeignClientService() {
        assertThat(keycloakFeignClientService)
                .hasFieldOrPropertyWithValue("keyCloakFeignClient", keyCloakFeignClient)
                .hasFieldOrPropertyWithValue("requestPropertiesKeycloak", requestPropertiesKeycloak)
        ;
    }

    @Test
    void getBearerToken_shouldReturnPreparedToken() {
        final ResponseKeycloak responseKeycloak = TestData.getTestResponseKeycloak();

        when(keyCloakFeignClient.processingAuthData(requestPropertiesKeycloak))
                .thenReturn(responseKeycloak);

        assertThat(keycloakFeignClientService.getBearerToken())
                .isEqualTo(
                        String.join(
                                SPACE,
                                BEARER,
                                responseKeycloak.getAccessToken()
                        )
                );

        assertThat(byteArrayOutputStream.toString().trim())
                .contains("get keycloak token at: ");

        verify(keyCloakFeignClient).processingAuthData(requestPropertiesKeycloak);
    }

    @Test
    void getAuthDataFromKeycloak_shouldReturnToken() {
        final ResponseKeycloak responseKeycloak = TestData.getTestResponseKeycloak();

        when(keyCloakFeignClient.processingAuthData(requestPropertiesKeycloak))
                .thenReturn(responseKeycloak);

        assertThat(keycloakFeignClientService.getAuthDataFromKeycloak())
                .isEqualTo(responseKeycloak);

        assertThat(byteArrayOutputStream.toString().trim())
                .contains("get keycloak token at: ");

        verify(keyCloakFeignClient).processingAuthData(requestPropertiesKeycloak);
    }

    @EnableCaching
    @Configuration
    public static class TestConfig {

        @Bean
        public CacheManager cacheManager() {
            return new CaffeineCacheManager("token");
        }

    }

}