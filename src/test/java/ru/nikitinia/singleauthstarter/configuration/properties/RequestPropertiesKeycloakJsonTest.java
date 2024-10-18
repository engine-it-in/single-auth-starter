package ru.nikitinia.singleauthstarter.configuration.properties;

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

@ContextConfiguration(classes = {RequestPropertiesKeycloak.class})
@JsonTest
class RequestPropertiesKeycloakJsonTest {

    @Autowired
    private JacksonTester<RequestPropertiesKeycloak> jacksonTester;

    @Value("json/request-properties-keycloak.json")
    private Resource json;

    private final RequestPropertiesKeycloak object =
            TestData.getTestRequestPropertiesKeycloak();

    @Test
    void shouldSerializeObjectToJson() throws IOException {
        assertThat(jacksonTester.write(object))
                .isStrictlyEqualToJson(json);
    }

    @Test
    void shouldDeserializeJsonToObject() throws IOException {
        assertThat(jacksonTester.read(json))
                .usingRecursiveComparison()
                .isEqualTo(object);
    }

}