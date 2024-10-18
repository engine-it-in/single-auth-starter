package ru.nikitinia.singleauthstarter.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.util.List;

@UtilityClass
public class Constant {

    @UtilityClass
    public class ConfigParam {
        public static final Long PAUSE_FOR_RETRY = 300L;
    }

    @UtilityClass
    public class KeycloakSetting {
        public static final String CACHE_NAME_TOKEN_KEYCLOAK = "KeycloakToken";
        public static final long CACHE_SIZE = 1;

    }

    @UtilityClass
    public class KeycloakFeignClientValue {
        public static final String KEYCLOAK_NAME_FEIGN_CLIENT = "KeycloakClient";
        public static final String KEYCLOAK_QUALIFIER_FEIGN_CLIENT = "QualifierKeycloak";

    }

    @UtilityClass
    public class RetryerStatuses {
        public static final List<Integer> RETRYER_STATUS_LIST =
                List.of(
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.NOT_IMPLEMENTED.value(),
                        HttpStatus.BAD_GATEWAY.value()
                );
    }

}
