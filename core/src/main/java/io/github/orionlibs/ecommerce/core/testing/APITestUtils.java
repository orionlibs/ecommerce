package io.github.orionlibs.ecommerce.core.testing;

import static io.restassured.RestAssured.given;

import io.github.orionlibs.ecommerce.core.Logger;
import io.github.orionlibs.ecommerce.core.json.JSONService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class APITestUtils
{
    public Response makeGetAPICall(HttpHeaders headers)
    {
        Logger.info("[JUnit] making GET call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .response();
    }


    public Response makePostAPICall(Object objectToSave, HttpHeaders headers)
    {
        Logger.info("[JUnit] making POST call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .body(JSONService.convertObjectToJSON(objectToSave))
                        .when()
                        .post()
                        .then()
                        .extract()
                        .response();
    }


    public Response makePutAPICall(Object objectToSave, HttpHeaders headers)
    {
        Logger.info("[JUnit] making PUT call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .body(JSONService.convertObjectToJSON(objectToSave))
                        .when()
                        .put()
                        .then()
                        .extract()
                        .response();
    }


    public Response makePatchAPICall(Object objectToSave, HttpHeaders headers)
    {
        Logger.info("[JUnit] making PATCH call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .body(JSONService.convertObjectToJSON(objectToSave))
                        .when()
                        .patch()
                        .then()
                        .extract()
                        .response();
    }


    public Response makeDeleteAPICall(HttpHeaders headers)
    {
        Logger.info("[JUnit] making DELETE call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .when()
                        .delete()
                        .then()
                        .extract()
                        .response();
    }


    private HttpHeaders getHttpHeaders(HttpHeaders headers)
    {
        if(headers == null)
        {
            headers = new HttpHeaders();
        }
        return headers;
    }
}
