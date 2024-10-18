package ru.nikitinia.singleauthstarter.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseKeycloak {

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private Integer expiresIn;

        @JsonProperty("refresh_expires_in")
        private Integer refreshExpiresIn;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("not-before-policy")
        private Integer notBeforePolicy;

        private String scope;
}
