package ru.nikitinia.singleauthstarter.configuration.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import feign.form.FormProperty;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "keycloak-request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestPropertiesKeycloak {
    @FormProperty("client_id")
    @JsonProperty("client_id")
    private String clientId;

    @FormProperty("grant_type")
    @JsonProperty("grant_type")
    private String grantType;

    @FormProperty("client_secret")
    @JsonProperty("client_secret")
    private String clientSecret;

}