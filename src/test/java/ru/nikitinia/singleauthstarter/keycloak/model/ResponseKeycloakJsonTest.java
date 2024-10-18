package ru.nikitinia.singleauthstarter.keycloak.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import ru.nikitinia.singleauthstarter.util.TestData;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ResponseKeycloak.class})
@JsonTest
class ResponseKeycloakJsonTest {

    @Autowired
    private JacksonTester<ResponseKeycloak> jacksonTester;

    @Value("json/response-keycloak.json")
    private Resource json;

    private final ResponseKeycloak object =
            TestData.getTestResponseKeycloak();

    @Test
    void shouldDeserializeJsonToObject() throws IOException {
        assertThat(jacksonTester.read(json))
                .usingRecursiveComparison()
                .isEqualTo(object);
    }

}