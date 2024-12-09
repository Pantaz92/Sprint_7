package ru.qascooter.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.qascooter.core.RestClient;
import ru.qascooter.dto.courier.Courier;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasKey;
import static org.apache.http.HttpStatus.*;

public class CourierApi {

    private final RestClient restClient;

    private static final String COURIER_CREATE_ENDPOINT = "/api/v1/courier";
    private static final String COURIER_LOGIN_ENDPOINT = "/api/v1/courier/login";
    private static final String COURIER_DELETE_ENDPOINT = "/api/v1/courier/%s";
    public static final String EXPECTED_409_CREATE_ERROR_MESSAGE = "Этот логин уже используется";
    public static final String EXPECTED_400_CREATE_ERROR_MESSAGE = "Недостаточно данных для создания учетной записи";
    public static final String EXPECTED_404_LOGIN_ERROR_MESSAGE = "Учетная запись не найдена";
    public static final String EXPECTED_400_LOGIN_ERROR_MESSAGE = "Недостаточно данных для входа";

    public CourierApi(RestClient restClient) {
        this.restClient = restClient;
    }

    @Step("Создаём курьера с данными: {courier}")
    public Response createCourier(Map<String, String> headers, Courier courier) {
        return restClient.postRequest(COURIER_CREATE_ENDPOINT, headers, courier);
    }

    @Step("Удаляем курьера после теста с ID: {courierId}")
    public Response deleteCourier(Map<String, String> headers, String courierId) {
        return restClient.deleteRequest(String.format(COURIER_DELETE_ENDPOINT, courierId), headers);
    }

    @Step("Логинимся курьером с логином: {courier.login} и пароем: {courier.password}")
    public Response loginCourier(Map<String, String> headers, Courier courier) {
        return restClient.postRequest(COURIER_LOGIN_ENDPOINT, headers, courier);
    }

    public String getCourierId(Map<String, String> headers, Courier courier) {
        Response loginResponse = loginCourier(headers, new Courier(courier.getLogin(), courier.getPassword()));
        loginResponse.then()
                .statusCode(SC_OK)
                .and()
                .body("$", hasKey("id"))
                .and()
                .body("id", is(notNullValue()));
        return loginResponse.jsonPath().getString("id");
    }

    public void assertStatusCode(Response response, int expectedStatusCode) {
        response.then()
                .statusCode(expectedStatusCode);
    }
}