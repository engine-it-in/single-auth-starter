package ru.nikitinia.singleauthstarter.keycloak.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakSetting.CACHE_NAME_TOKEN_KEYCLOAK;
import static ru.nikitinia.singleauthstarter.util.TestConstant.Main.TEST_STRING;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class KeycloakFeignClientServiceCacheTest {

    private KeycloakFeignClientService mock;

    @Autowired
    private KeycloakFeignClientService keycloakFeignClientService;

    @BeforeEach
    void setUp() {
        mock = AopTestUtils.getTargetObject(keycloakFeignClientService);
        reset(mock);
        when(mock.getBearerToken()).thenReturn(TEST_STRING);
    }

    @Test
    void testCache() {
        assertEquals(TEST_STRING, keycloakFeignClientService.getBearerToken());
        verify(mock, times(1)).getBearerToken();

        assertEquals(TEST_STRING, keycloakFeignClientService.getBearerToken());
        assertEquals(TEST_STRING, keycloakFeignClientService.getBearerToken());
        verifyNoMoreInteractions(mock);

    }

    @EnableCaching
    @Configuration
    public static class TestConfig {

        @Bean
        public KeycloakFeignClientService keycloakFeignClientServiceImplementation() {
            return Mockito.mock(KeycloakFeignClientService.class);
        }

        @Bean
        public CacheManager cacheManager() {
            return new CaffeineCacheManager(CACHE_NAME_TOKEN_KEYCLOAK);
        }

    }

}
