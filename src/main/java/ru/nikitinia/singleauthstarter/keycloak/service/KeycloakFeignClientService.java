package ru.nikitinia.singleauthstarter.keycloak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.nikitinia.singleauthstarter.configuration.properties.RequestPropertiesKeycloak;
import ru.nikitinia.singleauthstarter.keycloak.client.KeyCloakFeignClient;
import ru.nikitinia.singleauthstarter.keycloak.model.ResponseKeycloak;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor.BEARER;
import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakSetting.CACHE_NAME_TOKEN_KEYCLOAK;


@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(RequestPropertiesKeycloak.class)
@Slf4j
public class KeycloakFeignClientService {

    private final KeyCloakFeignClient keyCloakFeignClient;

    private final RequestPropertiesKeycloak requestPropertiesKeycloak;

    @Cacheable(cacheNames = CACHE_NAME_TOKEN_KEYCLOAK)
    public String getBearerToken() {
        return getPreparedBearerToken();
    }

    private String getPreparedBearerToken() {
        return String.join(
                SPACE,
                BEARER,
                getAuthDataFromKeycloak().getAccessToken()
        );
    }

    public ResponseKeycloak getAuthDataFromKeycloak() {
        log.info("get keycloak token at: {}", new Date());
        return keyCloakFeignClient.processingAuthData(requestPropertiesKeycloak);
    }

}
