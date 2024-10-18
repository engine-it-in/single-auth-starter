package ru.nikitinia.singleauthstarter.keycloak.configuration;

import feign.Retryer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakConnectException;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakProcessingException;
import ru.nikitinia.singleauthstarter.util.TestData;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class KeycloakRetryerTest {

    private final KeycloakRetryer retryer =
            new KeycloakRetryer();

    @Test
    void checkKeycloakRetryer() {
        assertThat(retryer)
                .isInstanceOf(Retryer.class)
                .hasFieldOrPropertyWithValue("maxAttempts", 2)
                .hasFieldOrPropertyWithValue("currentAttempt", 1)
        ;
    }

    @Test
    void checkKeycloakRetryer_shouldContainsDefaultRetryer() {

        final Retryer retryerFeign = new Retryer.Default(1000, TimeUnit.SECONDS.toMillis(1), 3);

        assertThat(retryer.getDefaultRetryer())
                .usingRecursiveComparison()
                .isEqualTo(retryerFeign)
        ;
    }

    @Test
    void clone_shouldReturnKeycloakRetryer() {

        assertThat(retryer.clone())
                .usingRecursiveComparison()
                .isEqualTo(new KeycloakRetryer())
        ;
    }

    @ParameterizedTest
    @ValueSource(ints = {401, 403, 500, 501, 502})
    void continueOrPropagate_shouldReturnResult_afterOneAttempt(int status) {

        final KeycloakConnectException keycloakConnectException = TestData.getTestKeycloakConnectExceptionWithStatus(status);

        retryer.continueOrPropagate(keycloakConnectException);

        assertThat(retryer.getCurrentAttempt())
                .isEqualTo(2)
        ;

    }

    @ParameterizedTest
    @ValueSource(ints = {401, 403, 500, 501, 502})
    void continueOrPropagate_shouldReturnResult_afterTwoAttempt(int status) {

        final KeycloakConnectException keycloakConnectException = TestData.getTestKeycloakConnectExceptionWithStatus(status);

        retryer.continueOrPropagate(keycloakConnectException);

        assertThatThrownBy(() -> retryer.continueOrPropagate(keycloakConnectException))
                .isInstanceOf(KeycloakProcessingException.class)
                .hasMessage(
                        String.format(
                                "Keycloak. Ошибка при попытке получить данные. Выполнено %d попыток. Статус: %d, содержание: %s",
                                retryer.getMaxAttempts(),
                                keycloakConnectException.status(),
                                keycloakConnectException.getMessage()
                        )
                )
        ;

    }

    @Test
    void continueOrPropagate_shouldReturnDefaultRetryer() {

        final KeycloakConnectException keycloakConnectException = TestData.getTestKeycloakConnectExceptionWithStatus(400);

        retryer.continueOrPropagate(keycloakConnectException);

        assertThat(retryer.getDefaultRetryer().clone())
                .isInstanceOf(Retryer.class);

        assertThat(retryer.getCurrentAttempt())
                .isEqualTo(1);

        assertThat(retryer.getMaxAttempts())
                .isEqualTo(2);
    }

}