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
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasKey;
import static ru.qascooter.api.CourierApi.EXPECTED_409_CREATE_ERROR_MESSAGE;

import java.util.Map;

public class CreateCourierTest extends BaseTest {

    private CourierApi courierApi;
    private Map<String, String> headers;
    private String courierId;
    private Courier courier;

    @Before
    @Override
    public void setUp() {
        super.setUp();

        courierApi = new CourierApi(restClient);

        headers = Map.of("Content-Type", "application/json");

        Faker faker = new Faker();
        String fakeLogin = faker.internet().emailAddress();
        String fakePassword = faker.internet().password();
        String fakeFirstName = faker.name().firstName();
        courier = new Courier(fakeLogin, fakePassword, fakeFirstName);
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            Response response = courierApi.deleteCourier(headers, courierId);
            response.then()
                    .statusCode(SC_OK);
        }
    }

    @Test
    @Description("Проверяем что код ответа 201 и в ответе параметр ок со значением true")
    @DisplayName("Успешное создание курьера со всеми параметрами")
    public void testCreateCourierWithAllFieldsSuccess() {
        Response response = courierApi.createCourier(headers, courier);
        courierApi.assertStatusCode(response, SC_CREATED);
        response.then()
                .body("$", hasKey("ok"))
                .and()
                .body("ok", is(true));

        courierId = courierApi.getCourierId(headers, courier);
    }

    @Test
    @Description("Проверяем что код ответа 201 и в ответе параметр ок со значением true")
    @DisplayName("Успешное создание курьера с логином и паролем")
    public void testCreateCourierWithLoginAndPasswordSuccess() {
        courier.setFirstName(null);
        Response response = courierApi.createCourier(headers, courier);
        courierApi.assertStatusCode(response, SC_CREATED);
        response.then()
                .body("$", hasKey("ok"))
                .and()
                .body("ok", is(true));

        courierId = courierApi.getCourierId(headers, courier);
    }

    @Test
    @Description("Проверяем что код ответа 409 и в ответе верный текст ошибки")
    @DisplayName("Ошибка при создании курьера с существующим логином")
    public void testCreateExistenceCourierError() {
        Response firstCreateResponse = courierApi.createCourier(headers, courier);
        courierApi.assertStatusCode(firstCreateResponse, SC_CREATED);

        courierId = courierApi.getCourierId(headers, courier);

        Response secondCreateResponse = courierApi.createCourier(headers, courier);
        courierApi.assertStatusCode(secondCreateResponse, SC_CONFLICT);
        secondCreateResponse.then()
                .body("$", hasKey("message"))
                .and()
                .body("message", is(EXPECTED_409_CREATE_ERROR_MESSAGE));
    }
}
