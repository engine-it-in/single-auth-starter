package ru.nikitinia.singleauthstarter.authorize;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.nikitinia.singleauthstarter.keycloak.service.KeycloakFeignClientService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class KeyCloakRequestInterceptor implements RequestInterceptor {

    private final KeycloakFeignClientService keycloakFeignClientService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .headers()
                .getOrDefault(HttpHeaders.AUTHORIZATION, List.of())
                .stream()
                .findFirst()
                .ifPresentOrElse(
                        token -> {
                            final Map<String, Collection<String>> headers = new HashMap<>(requestTemplate.headers());
                            headers.put(HttpHeaders.AUTHORIZATION, List.of(keycloakFeignClientService.getBearerToken()));
                            requestTemplate.headers(headers);
                        },
                        () ->
                                requestTemplate.header(HttpHeaders.AUTHORIZATION, keycloakFeignClientService.getBearerToken())
                );

    }
}
