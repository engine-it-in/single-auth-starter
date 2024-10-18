package ru.nikitinia.singleauthstarter.util;

import feign.RequestTemplate;
import ru.nikitinia.singleauthstarter.configuration.properties.RequestPropertiesKeycloak;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakConnectException;
import ru.nikitinia.singleauthstarter.keycloak.model.ResponseKeycloak;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static ru.nikitinia.singleauthstarter.util.TestConstant.Main.TEST_INT;
import static ru.nikitinia.singleauthstarter.util.TestConstant.Main.TEST_STRING;

public class TestData {

    public static RequestPropertiesKeycloak getTestRequestPropertiesKeycloak() {
        return RequestPropertiesKeycloak.builder()
                .grantType(TEST_STRING)
                .clientId(TEST_STRING)
                .clientSecret(TEST_STRING)
                .build();
    }

    public static feign.Response getTestFeignResponsePostWithStatusAndBodyFromParameters(int status, String body) {
        return feign.Response.builder()
                .request(feign.Request
                        .create(
                                feign.Request.HttpMethod.POST,
                                TEST_STRING,
                                new HashMap<>(),
                                feign.Request.Body.create(body).asBytes(),
                                StandardCharsets.UTF_8,
                                null
                        )
                )
                .status(status)
                .body(TEST_STRING, StandardCharsets.UTF_8)
                .build();

    }

    public static feign.Request getTestFeignRequest() {
        return feign.Request
                .create(
                        feign.Request.HttpMethod.POST,
                        TEST_STRING,
                        new HashMap<>(),
                        new byte[123],
                        StandardCharsets.UTF_8,
                        new RequestTemplate()
                );
    }

    public static KeycloakConnectException getTestKeycloakConnectExceptionWithStatus(int status) {
        return new KeycloakConnectException(
                status,
                TEST_STRING,
                feign.Request.HttpMethod.POST,
                1L,
                getTestFeignRequest()
        );
    }

    public static ResponseKeycloak getTestResponseKeycloak() {
        return ResponseKeycloak.builder()
                .accessToken(TEST_STRING)
                .expiresIn(TEST_INT)
                .refreshExpiresIn(TEST_INT)
                .tokenType(TEST_STRING)
                .notBeforePolicy(TEST_INT)
                .scope(TEST_STRING)
                .build();
    }

}
