package ru.nikitinia.singleauthstarter.authorize;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.nikitinia.singleauthstarter.keycloak.service.KeycloakFeignClientService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static ru.nikitinia.singleauthstarter.util.TestConstant.Main.TEST_STRING;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KeyCloakRequestInterceptor.class)
class KeyCloakRequestInterceptorTest {

    @Autowired
    private KeyCloakRequestInterceptor keyCloakRequestInterceptor;

    @MockBean
    private KeycloakFeignClientService keycloakFeignClientService;

    @Test
    void checkKeyCloakRequestInterceptor() {
        assertThat(keyCloakRequestInterceptor)
                .hasFieldOrPropertyWithValue("keycloakFeignClientService", keycloakFeignClientService);
    }

    @Test
    void addAuthorizeDataToRequest_shouldGetDataFromKeycloakService_becauseFirstTime() {
        final RequestTemplate requestTemplate = new RequestTemplate();

        when(keycloakFeignClientService.getBearerToken())
                .thenReturn(TEST_STRING);

        assertThat(keyCloakRequestInterceptor.addAuthorizeDataToRequest())
                .isInstanceOfSatisfying(RequestInterceptor.class, requestInterceptor -> {

                    requestInterceptor.apply(requestTemplate);

                    assertThat(requestTemplate.headers())
                            .containsEntry(AUTHORIZATION, List.of(TEST_STRING));

                });

        verify(keycloakFeignClientService).getBearerToken();

    }

    @Test
    void addAuthorizeDataToRequest_shouldGetDataFromKeycloakService_becauseReNew() {
        final String token = TEST_STRING;
        final RequestTemplate requestTemplate = new RequestTemplate();
        final Map<String, Collection<String>> headers = new HashMap<>(requestTemplate.headers());
        headers.put(AUTHORIZATION, List.of(token));
        requestTemplate.headers(headers);

        when(keycloakFeignClientService.getBearerToken())
                .thenReturn(token);

        assertThat(keyCloakRequestInterceptor.addAuthorizeDataToRequest())
                .isInstanceOfSatisfying(RequestInterceptor.class, requestInterceptor -> {

                    requestInterceptor.apply(requestTemplate);

                    assertThat(requestTemplate.headers())
                            .containsEntry(AUTHORIZATION, List.of(TEST_STRING));

                });

        verify(keycloakFeignClientService).getBearerToken();

    }

}