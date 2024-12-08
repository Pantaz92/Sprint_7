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
import static ru.qascooter.api.CourierApi.EXPECTED_400_LOGIN_ERROR_MESSAGE;

@RunWith(Parameterized.class)
public class LoginCourierMissingFieldsTest extends BaseTest {

    private final Courier testCourier;
    private final String expectedErrorMessage;

    public LoginCourierMissingFieldsTest(Courier testCourier, String expectedErrorMessage) {
        this.testCourier = testCourier;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}, Ожидаемое сообщение: {1}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {new Courier(null, "password123"), EXPECTED_400_LOGIN_ERROR_MESSAGE},
                {new Courier("login123", null), EXPECTED_400_LOGIN_ERROR_MESSAGE}
        };
    }

    @Test
    @Description("Проверяем, что авторизация без обязательных полей возвращает 400 ошибку и верное сообщение об ошибке")
    @DisplayName("Ошибка при отсутствии обязательных полей")
    public void testLoginCourierWithMissingFieldsError() {
        CourierApi courierApi = new CourierApi(restClient);
        Map<String, String> headers = Map.of("Content-Type", "application/json");

        Response loginResponse = courierApi.loginCourier(headers, testCourier);
        loginResponse.then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("$", hasKey("message"))
                .and()
                .body("message", is(expectedErrorMessage));
    }
}
