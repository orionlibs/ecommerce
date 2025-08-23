package io.github.orionlibs.ecommerce.lifecycle.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.ecommerce.core.testing.APITestUtils;
import io.github.orionlibs.ecommerce.lifecycle.ControllerUtils;
import io.github.orionlibs.ecommerce.lifecycle.LifecycleService;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinitionModel;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleInstanceModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SaveStateTransitionWithAutomaticDetectionOfNextStateAPIControllerTest
{
    @LocalServerPort int port;
    @Autowired LifecycleService lifecycleService;
    @Autowired APITestUtils apiUtils;
    HttpHeaders headers;


    @BeforeEach
    public void setUp()
    {
        lifecycleService.deleteAllDefinitions();
        lifecycleService.deleteAllInstances();
        headers = new HttpHeaders();
        RestAssured.baseURI = "http://localhost:" + port + ControllerUtils.baseAPIPath + "/lifecycles/instances";
    }


    @Test
    void saveStateTransitionWithAutomaticDetectionOfNextState()
    {
        LifecycleDefinitionModel definition = new LifecycleDefinitionModel();
        definition.setKey("key1");
        definition.setName("name1");
        definition.setPayload("""
                        key: "key1"
                        name: "name1"
                        version: 1
                        states:
                          - "STATE_1"
                          - "STATE_2"
                          - "STATE_3"
                          - "STATE_4"
                        transitions:
                          - name: "transition1"
                            from: "STATE_1"
                            to: "STATE_2"
                          - name: "transition2"
                            from: "STATE_2"
                            to: "STATE_3"
                          - name: "transition3"
                            from: "STATE_2"
                            to: "STATE_4"
                        """);
        definition.setVersion(1);
        definition = lifecycleService.saveDefinition(definition);
        LifecycleInstanceModel instance = lifecycleService.initialiseLifecycle(definition);
        RestAssured.baseURI += "/" + instance.getId().toString() + "/transitions";
        Response response = apiUtils.makePostAPICall(null, headers);
        assertThat(response.statusCode()).isEqualTo(201);
    }
}
