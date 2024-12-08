package ru.qascooter.tests.couriertests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.qascooter.api.CourierApi;
import ru.qascooter.core.BaseTest;
import ru.qascooter.dto.courier.Courier;

import java.util.Map;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasKey;
import static ru.qascooter.api.CourierApi.EXPECTED_400_CREATE_ERROR_MESSAGE;

@RunWith(Parameterized.class)
public class CreateCourierMissingFieldsTest extends BaseTest {

    private final Courier testCourier;
    private final String expectedErrorMessage;

    public CreateCourierMissingFieldsTest(Courier testCourier, String expectedErrorMessage) {
        this.testCourier = testCourier;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}, Ожидаемое сообщение: {1}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {new Courier(null, "password123", "John"), EXPECTED_400_CREATE_ERROR_MESSAGE},
                {new Courier("test@example.com", null, "John"), EXPECTED_400_CREATE_ERROR_MESSAGE}
        };
    }

    @Test
    @Description("Проверяем, что создание курьера без обязательных полей возвращает 400 ошибку и верное сообщение об ошибке")
    @DisplayName("Создание курьера без обязательных полей")
    public void testCreateCourierWithMissingFieldsError() {
        CourierApi courierApi = new CourierApi(restClient);
        Map<String, String> headers = Map.of("Content-Type", "application/json");

        Response response = courierApi.createCourier(headers, testCourier);
        response.then().statusCode(SC_BAD_REQUEST)
                .and()
                .body("$", hasKey("message"))
                .and()
                .body("message", is(expectedErrorMessage));
    }
}
