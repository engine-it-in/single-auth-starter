package ru.nikitinia.singleauthstarter.configuration.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nikitinia.singleauthstarter.configuration.properties.KeycloakConnectionSettings;

import java.util.concurrent.TimeUnit;

import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakSetting.CACHE_NAME_TOKEN_KEYCLOAK;
import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakSetting.CACHE_SIZE;

@Configuration
@EnableCaching
@EnableConfigurationProperties(KeycloakConnectionSettings.class)
@RequiredArgsConstructor
public class CacheTokenConfig {

    private final KeycloakConnectionSettings keycloakConnectionSettings;

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(
                        keycloakConnectionSettings.cachetime(),
                        TimeUnit.SECONDS
                ).maximumSize(CACHE_SIZE)
                ;
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CACHE_NAME_TOKEN_KEYCLOAK);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }


}