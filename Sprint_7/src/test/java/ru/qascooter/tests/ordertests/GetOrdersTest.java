package ru.qascooter.tests.ordertests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.qascooter.api.OrderApi;
import ru.qascooter.core.BaseTest;

import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class GetOrdersTest extends BaseTest {

    private OrderApi orderApi;
    private Map<String, String> headers;
    private int limitOrdersPerPage;

    @Override
    public void setUp() {
        super.setUp();
        orderApi = new OrderApi(restClient);
        headers = Map.of("Content-Type", "application/json");
        limitOrdersPerPage = 5;

    }

    @Test
    @Description("Проверяем, что в ответе возвращается список заказов")
    @DisplayName("Получение списка заказов")
    public void GetOrdersListWith5OrdersSUCCESS() {
        Response response = orderApi.getOrders(headers, limitOrdersPerPage);

        response.then()
                .statusCode(SC_OK)
                .and()
                .body("orders", is(not(empty())))
                .and()
                .body("orders", hasSize(greaterThan(0)));
    }
}
