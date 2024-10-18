package ru.nikitinia.singleauthstarter.configuration.properties;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "keycloak-connection")
@Builder
public record KeycloakConnectionSettings(

        Long cachetime

) {
}
