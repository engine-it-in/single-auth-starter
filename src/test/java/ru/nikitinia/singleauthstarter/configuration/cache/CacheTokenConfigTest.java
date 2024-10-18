package ru.nikitinia.singleauthstarter.configuration.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nikitinia.singleauthstarter.configuration.properties.KeycloakConnectionSettings;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakSetting.CACHE_NAME_TOKEN_KEYCLOAK;
import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakSetting.CACHE_SIZE;

@ExtendWith(MockitoExtension.class)
class CacheTokenConfigTest {

    private CacheTokenConfig cacheTokenConfig;

    @Mock
    private KeycloakConnectionSettings keycloakConnectionSettings;

    private final Long cachetime = 1L;

    private final Caffeine caffeine = Caffeine.newBuilder()
            .expireAfterWrite(
                    cachetime,
                    TimeUnit.SECONDS
            ).maximumSize(CACHE_SIZE);

    @BeforeEach
    void setUp() {
        cacheTokenConfig = new CacheTokenConfig(keycloakConnectionSettings);
    }


    @Test
    void checkCacheConfig() {

        assertThat(cacheTokenConfig)
                .hasFieldOrPropertyWithValue("keycloakConnectionSettings", keycloakConnectionSettings)
        ;
    }

    @Test
    void caffeineConfig_shouldReturnPrepareBeanCaffeine() {

        when(keycloakConnectionSettings.cachetime()).thenReturn(cachetime);

        assertThat(cacheTokenConfig.caffeineConfig())
                .usingRecursiveComparison()
                .isEqualTo(caffeine)
        ;

        verify(keycloakConnectionSettings).cachetime();

    }

    @Test
    void caffeineConfig_shouldReturnPrepareBeanCacheManager() {

        assertThat(cacheTokenConfig.cacheManager(caffeine))
                .isInstanceOfSatisfying(CacheManager.class, cacheManager -> {

                    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CACHE_NAME_TOKEN_KEYCLOAK);
                    caffeineCacheManager.setCaffeine(caffeine);

                    assertThat(cacheManager)
                            .usingRecursiveComparison()
                            .isEqualTo(caffeineCacheManager);

                })
        ;

    }


    @EnableCaching
    @Configuration
    public static class CachingTestConfig {

        @Bean
        public CacheManager cacheManager() {
            return new CaffeineCacheManager(CACHE_NAME_TOKEN_KEYCLOAK);
        }

    }

}