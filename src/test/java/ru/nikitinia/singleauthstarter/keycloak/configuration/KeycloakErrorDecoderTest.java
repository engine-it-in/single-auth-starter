package ru.nikitinia.singleauthstarter.keycloak.configuration;

import feign.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakConnectException;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakProcessingException;
import ru.nikitinia.singleauthstarter.util.TestData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static ru.nikitinia.singleauthstarter.util.Constant.ConfigParam.PAUSE_FOR_RETRY;
import static ru.nikitinia.singleauthstarter.util.TestConstant.Main.TEST_STRING;

class KeycloakErrorDecoderTest {

    private final KeycloakErrorDecoder keycloakErrorDecoder =
            new KeycloakErrorDecoder();

    @ParameterizedTest
    @ValueSource(ints = {400, 450, 503, 550})
    void decode_shouldKeycloakProcessingException(int status) {
        final String methodKey = TEST_STRING;
        final String body = TEST_STRING;
        final Response response = TestData.getTestFeignResponsePostWithStatusAndBodyFromParameters(status, body);

        assertThat(keycloakErrorDecoder.decode(methodKey, response))
                .isInstanceOf(KeycloakProcessingException.class)
                .hasMessageContaining(
                        String.format(
                                "Keycloak. Ошибка при попытке получить данные. Статус: %d, содержание: %s",
                                response.status(),
                                methodKey
                        )
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {401, 403, 500, 501, 502})
    void decode_shouldKeycloakConnectionException(int status) throws IOException {
        final String methodKey = TEST_STRING;
        final String body = TEST_STRING;
        final Response response = TestData.getTestFeignResponsePostWithStatusAndBodyFromParameters(status, body);
        final KeycloakConnectException exception =
                new KeycloakConnectException(
                        response.status(),
                        response.body().asInputStream().toString(),
                        response.request().httpMethod(),
                        PAUSE_FOR_RETRY,
                        response.request()
                );

        assertThat(keycloakErrorDecoder.decode(methodKey, response))
                .usingRecursiveComparison()
                .isEqualTo(exception);
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 403, 500, 501, 503, 550})
    void decode_shouldThrownIOException_becauseProblemWithBody(int status) {

        String body = TEST_STRING;
        final Response response = TestData.getTestFeignResponsePostWithStatusAndBodyFromParameters(status, body);
        final IOException exception = new IOException(TEST_STRING);


        try (MockedStatic<IOUtils> ioUtilsMockedStatic = mockStatic(IOUtils.class)) {

            ioUtilsMockedStatic.when(() -> IOUtils.toString(any(InputStream.class), any(Charset.class)))
                    .thenThrow(exception);

            assertThat(keycloakErrorDecoder.decode(TEST_STRING, response))
                    .isInstanceOf(KeycloakProcessingException.class)
                    .hasMessage(String.format(
                            "Keycloak. Ошибка при попытке получить данные. Статус: %d, содержание: %s",
                            response.status(),
                            TEST_STRING
                    ));

            ioUtilsMockedStatic.verify(() -> IOUtils.toString(any(InputStream.class), any(Charset.class)));

        }
    }

}