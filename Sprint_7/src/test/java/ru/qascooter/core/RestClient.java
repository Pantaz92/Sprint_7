package ru.qascooter.core;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class RestClient {
    private final Gson gson;

    public RestClient(Gson gson) {
        this.gson = gson;
    }

    public Response getRequest(String endpoint, Map<String, String> headers) {
        return RestAssured.given()
                .headers(headers)
                .when()
                .get(endpoint);
    }

    public Response postRequest(String endpoint, Map<String, String> headers, Object body) {
        String jsonBody = gson.toJson(body);
        return RestAssured.given()
                .headers(headers)
                .and()
                .body(jsonBody)
                .when()
                .post(endpoint);
    }

    public Response putRequest(String endpoint, Map<String, String> headers, Object body) {
        String jsonBody = gson.toJson(body);
        return RestAssured.given()
                .headers(headers)
                .and()
                .body(jsonBody)
                .when()
                .put(endpoint);
    }

    public Response deleteRequest(String endpoint, Map<String, String> headers) {
        return RestAssured.given()
                .headers(headers)
                .when()
                .delete(endpoint);
    }
}
