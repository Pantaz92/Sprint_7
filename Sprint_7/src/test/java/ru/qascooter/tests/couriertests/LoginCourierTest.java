package ru.qascooter.tests.couriertests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.qascooter.api.CourierApi;
import ru.qascooter.core.BaseTest;
import ru.qascooter.dto.courier.Courier;

import java.util.Map;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;
import static ru.qascooter.api.CourierApi.EXPECTED_404_LOGIN_ERROR_MESSAGE;

public class LoginCourierTest extends BaseTest {

    private CourierApi courierApi;
    private Map<String, String> headers;
    private Courier courier;
    private String courierId = null;
    private String validLogin;
    private String validPassword;

    @Before
    @Override
    public void setUp() {
        super.setUp();

        courierApi = new CourierApi(restClient);
        headers = Map.of("Content-Type", "application/json");

        Faker faker = new Faker();
        validLogin = faker.internet().emailAddress();
        validPassword = faker.internet().password();
        courier = new Courier(validLogin, validPassword);
        Response createResponse = courierApi.createCourier(headers, courier);
        courierApi.assertStatusCode(createResponse, SC_CREATED);
    }

    @After
    public void tearDown() {
        if (courierId == null) {
            courier.setLogin(validLogin);
            courier.setPassword(validPassword);
            Response loginResponse = courierApi.loginCourier(headers, courier);
            if (loginResponse.statusCode() == SC_OK) {
                courierId = loginResponse.jsonPath().getString("id");
            }
        }
        if (courierId != null) {
            Response response = courierApi.deleteCourier(headers, courierId);
            response.then()
                    .statusCode(SC_OK);
        }
    }

    @Test
    @Description("Проверяем, что курьер может успешно авторизоваться с валидными полями")
    @DisplayName("Успешная авторизация курьера")
    public void loginCourierSuccess() {
        Response loginResponse = courierApi.loginCourier(headers, courier);
        courierApi.assertStatusCode(loginResponse, SC_OK);
        loginResponse.then()
                .body("$", hasKey("id"))
                .and()
                .body("id", is(notNullValue()));
        courierId = loginResponse.jsonPath().getString("id");
    }

    @Test
    @Description("Проверяем, что система вернет ошибку при авторизации с неверным логином")
    @DisplayName("Ошибка при авторизации с неверным логином")
    public void loginCourierWithInvalidLogin() {
        courier.setLogin("wrongLogin");
        Response loginResponse = courierApi.loginCourier(headers, courier);
        courierApi.assertStatusCode(loginResponse, SC_NOT_FOUND);
        loginResponse.then()
                .body("$", hasKey("message"))
                .and()
                .body("message", is(EXPECTED_404_LOGIN_ERROR_MESSAGE));
    }

    @Test
    @Description("Проверяем, что система вернет ошибку при авторизации с неверным паролем")
    @DisplayName("Ошибка при авторизации с неверным паролем")
    public void loginCourierWithInvalidPassword() {
        courier.setPassword("wrongPassword");
        Response loginResponse = courierApi.loginCourier(headers, courier);
        courierApi.assertStatusCode(loginResponse, SC_NOT_FOUND);
        loginResponse.then()
                .body("$", hasKey("message"))
                .and()
                .body("message", is(EXPECTED_404_LOGIN_ERROR_MESSAGE));
    }

    @Test
    @Description("Проверяем, что система вернет ошибку при авторизации с несуществующим логином и паролем")
    @DisplayName("Ошибка при авторизации с неверным паролем")
    public void loginCourierWithInvalidLoginAndPassword() {
        courier.setLogin("wrongLogin");
        courier.setPassword("wrongPassword");
        Response loginResponse = courierApi.loginCourier(headers, courier);
        courierApi.assertStatusCode(loginResponse, SC_NOT_FOUND);
        loginResponse.then()
                .body("$", hasKey("message"))
                .and()
                .body("message", is(EXPECTED_404_LOGIN_ERROR_MESSAGE));
    }
}
