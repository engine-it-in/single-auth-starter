package ru.nikitinia.singleauthstarter.keycloak.exception;

public class KeycloakProcessingException extends RuntimeException {
    public KeycloakProcessingException(String message) {
        super(message);
    }
}
