package ru.nikitinia.singleauthstarter.keycloak.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import ru.nikitinia.singleauthstarter.configuration.properties.RequestPropertiesKeycloak;
import ru.nikitinia.singleauthstarter.keycloak.configuration.KeycloakFeignClientConfiguration;
import ru.nikitinia.singleauthstarter.keycloak.model.ResponseKeycloak;
import ru.nikitinia.singleauthstarter.util.TestData;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakFeignClientValue.KEYCLOAK_NAME_FEIGN_CLIENT;
import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakFeignClientValue.KEYCLOAK_QUALIFIER_FEIGN_CLIENT;

@SpringBootTest(
        classes = {
                KeyCloakFeignClient.class,
                ObjectMapper.class
        },

        properties = {
                "keycloak-connection.url=http://localhost:${wiremock.server.port}/urlPath"
        }
)
@AutoConfigureWireMock(port = 0)
@ImportAutoConfiguration(classes = {
        FeignAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class
})
@EnableFeignClients(clients = {KeyCloakFeignClient.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class KeyCloakFeignClientTest {

    @Autowired
    private KeyCloakFeignClient client;

    @Autowired
    private ObjectMapper mapper;

    @Value("/urlPath")
    private String urlPath;

    @Test
    void checkKeyCloakFeignClient() {
        FeignClient feignClientAnnotation =
                KeyCloakFeignClient.class.getAnnotation(FeignClient.class);

        assertThat(feignClientAnnotation)
                .isInstanceOfSatisfying(FeignClient.class, feignClient -> {

                    assertThat(feignClient.name())
                            .isEqualTo(KEYCLOAK_NAME_FEIGN_CLIENT);

                    assertThat(feignClient.qualifiers())
                            .contains(KEYCLOAK_QUALIFIER_FEIGN_CLIENT);

                    assertThat(feignClient.url())
                            .isEqualTo("${keycloak-connection.url}");

                    assertThat(feignClient.configuration())
                            .contains(KeycloakFeignClientConfiguration.class);
                });
    }

    @Test
    void processingAuthData_shouldReturnResult() throws JsonProcessingException {
        final ResponseKeycloak responseKeycloak = TestData.getTestResponseKeycloak();
        final RequestPropertiesKeycloak requestPropertiesKeycloak = TestData.getTestRequestPropertiesKeycloak();

        stubFor(post(urlEqualTo(urlPath))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(mapper.writeValueAsString(responseKeycloak))
                )
        );

        assertThat(client.processingAuthData(requestPropertiesKeycloak))
                .usingRecursiveComparison()
                .isEqualTo(responseKeycloak);

        verify(postRequestedFor(urlEqualTo(urlPath)));
    }

    @EnableCaching
    @Configuration
    public static class CachingTestConfig {

        @Bean
        public CacheManager cacheManager() {
            return new CaffeineCacheManager();
        }

    }

}