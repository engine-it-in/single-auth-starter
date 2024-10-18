package ru.nikitinia.singleauthstarter.keycloak.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakConnectException;
import ru.nikitinia.singleauthstarter.util.TestData;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class KeycloakRetryerCacheEvictTest {

    @Spy
    private final KeycloakRetryer retryer =
            new KeycloakRetryer();

    private final ByteArrayOutputStream byteArrayOutputStream =
            new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(byteArrayOutputStream));
    }

    @ParameterizedTest
    @ValueSource(ints = {401, 403, 500, 501, 502})
    void continueOrPropagate_shouldEvictCacheToken(int status) {

        final KeycloakConnectException keycloakConnectException = TestData.getTestKeycloakConnectExceptionWithStatus(status);

        retryer.continueOrPropagate(keycloakConnectException);

        assertThat(retryer.getCurrentAttempt())
                .isEqualTo(2);

        verify(retryer).clearTokenCacheData();

        assertThat(byteArrayOutputStream.toString().trim())
                .contains("clear token data after get 4xx status");

    }

}