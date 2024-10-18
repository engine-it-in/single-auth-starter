package ru.nikitinia.singleauthstarter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.nikitinia.singleauthstarter.keycloak.client.KeyCloakFeignClient;

@Configuration
@ConditionalOnProperty("keycloak-connection.url")
@EnableConfigurationProperties
@EnableFeignClients(clients = KeyCloakFeignClient.class)
public class AuthorizationServiceClient {
}
