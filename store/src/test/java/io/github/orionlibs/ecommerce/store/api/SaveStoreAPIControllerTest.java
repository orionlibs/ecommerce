package io.github.orionlibs.ecommerce.store.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.ecommerce.core.api.error.APIError;
import io.github.orionlibs.ecommerce.core.testing.APITestUtils;
import io.github.orionlibs.ecommerce.store.ControllerUtils;
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
class SaveStoreAPIControllerTest
{
    @LocalServerPort int port;
    @Autowired APITestUtils apiUtils;
    String basePath;
    HttpHeaders headers;


    @BeforeEach
    public void setUp()
    {
        basePath = "http://localhost:" + port + ControllerUtils.baseAPIPath + "/stores";
    }


    @Test
    void saveStore()
    {
        RestAssured.baseURI = basePath;
        SaveStoreRequest request = SaveStoreRequest.builder()
                        .storeName("shop1")
                        .build();
        Response response = apiUtils.makePostAPICall(request, headers);
        assertThat(response.statusCode()).isEqualTo(201);
    }


    @Test
    void saveStore_invalidStoreName()
    {
        RestAssured.baseURI = basePath;
        SaveStoreRequest request = SaveStoreRequest.builder()
                        .storeName("")
                        .build();
        Response response = apiUtils.makePostAPICall(request, headers);
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        assertThat(body.message()).isEqualTo("Validation failed for one or more fields");
        assertThat(body.fieldErrors().get(0).message()).isEqualTo("storeName must not be blank");
    }
}
