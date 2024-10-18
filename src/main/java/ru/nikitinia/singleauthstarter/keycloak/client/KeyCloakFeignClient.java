package ru.nikitinia.singleauthstarter.keycloak.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nikitinia.singleauthstarter.configuration.properties.RequestPropertiesKeycloak;
import ru.nikitinia.singleauthstarter.keycloak.configuration.KeycloakFeignClientConfiguration;
import ru.nikitinia.singleauthstarter.keycloak.model.ResponseKeycloak;

import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakFeignClientValue.KEYCLOAK_NAME_FEIGN_CLIENT;
import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakFeignClientValue.KEYCLOAK_QUALIFIER_FEIGN_CLIENT;

@FeignClient(
        name = KEYCLOAK_NAME_FEIGN_CLIENT,
        qualifiers = KEYCLOAK_QUALIFIER_FEIGN_CLIENT,
        url = "${keycloak-connection.url}",
        configuration = KeycloakFeignClientConfiguration.class
)
public interface KeyCloakFeignClient {

    @PostMapping(
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseKeycloak processingAuthData(RequestPropertiesKeycloak requestPropertiesKeycloak);

}