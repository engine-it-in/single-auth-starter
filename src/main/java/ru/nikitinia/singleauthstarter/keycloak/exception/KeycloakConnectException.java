package ru.nikitinia.singleauthstarter.keycloak.exception;

import feign.Request;
import feign.RetryableException;

public class KeycloakConnectException extends RetryableException {

    public KeycloakConnectException(int status, String message, Request.HttpMethod httpMethod, Long retryAfter, Request request) {
        super(status, message, httpMethod, retryAfter, request);
    }
}
