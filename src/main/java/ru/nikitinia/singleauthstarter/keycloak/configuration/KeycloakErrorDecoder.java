package ru.nikitinia.singleauthstarter.keycloak.configuration;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakConnectException;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakProcessingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ru.nikitinia.singleauthstarter.util.Constant.ConfigParam.PAUSE_FOR_RETRY;
import static ru.nikitinia.singleauthstarter.util.Constant.RetryerStatuses.RETRYER_STATUS_LIST;

public class KeycloakErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String message;
        try {
            message =
                    IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return new KeycloakProcessingException(
                    String.format(
                            "Keycloak. Ошибка при попытке получить данные. Статус: %d, содержание: %s",
                            response.status(),
                            methodKey
                    )
            );
        }

        if (RETRYER_STATUS_LIST.contains(response.status())) {
            return new KeycloakConnectException(
                    response.status(),
                    message,
                    response.request().httpMethod(),
                    PAUSE_FOR_RETRY,
                    response.request());
        } else {
            return new KeycloakProcessingException(
                    String.format(
                            "Keycloak. Ошибка при попытке получить данные. Статус: %d, содержание: %s",
                            response.status(),
                            message
                    )
            );
        }
    }

}
