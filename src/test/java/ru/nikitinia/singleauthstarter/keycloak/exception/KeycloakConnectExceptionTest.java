package ru.nikitinia.singleauthstarter.keycloak.exception;

import feign.Request;
import feign.RequestTemplate;
import feign.RetryableException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.nikitinia.singleauthstarter.util.TestConstant.Main.TEST_INT;
import static ru.nikitinia.singleauthstarter.util.TestConstant.Main.TEST_STRING;

class KeycloakConnectExceptionTest {

    private final Request request = Request
            .create(
                    Request.HttpMethod.POST,
                    TEST_STRING,
                    new HashMap<>(),
                    new byte[123],
                    StandardCharsets.UTF_8,
                    new RequestTemplate()
            );

    private final KeycloakConnectException keycloakConnectException =
            new KeycloakConnectException(
                    TEST_INT,
                    TEST_STRING,
                    Request.HttpMethod.POST,
                    1L,
                    request
            );

    @Test
    void checkKeycloakConnectException() {
        assertThat(keycloakConnectException)
                .isInstanceOf(RetryableException.class)
                .hasFieldOrPropertyWithValue("status", TEST_INT)
                .hasFieldOrPropertyWithValue("message", TEST_STRING)
                .hasFieldOrPropertyWithValue("httpMethod", Request.HttpMethod.POST)
                .hasFieldOrPropertyWithValue("retryAfter", 1L)
                .hasFieldOrPropertyWithValue("request", request)
        ;
    }

}