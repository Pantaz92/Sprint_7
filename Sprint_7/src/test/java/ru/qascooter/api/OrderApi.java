package ru.qascooter.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.qascooter.core.RestClient;
import ru.qascooter.dto.order.CancelOrder;
import ru.qascooter.dto.order.CreateOrder;

import java.util.Map;

public class OrderApi {

    private final RestClient restClient;

    private static final String ORDER_CREATE_ENDPOINT = "/api/v1/orders";
    private static final String ORDER_CANCEL_ENDPOINT = "/api/v1/orders/cancel";
    private static final String ORDERS_GET_ENDPOINT = "/api/v1/orders?limit=%d";

    public OrderApi(RestClient restClient) {
        this.restClient = restClient;
    }

    @Step("Созадние заказа")
    public Response createOrder(Map<String, String> headers, CreateOrder order) {
        return restClient.postRequest(ORDER_CREATE_ENDPOINT, headers, order);
    }

    @Step("Отмена заказа")
    public Response cancelOrder(Map<String, String> headers, CancelOrder order) {
        return restClient.putRequest(ORDER_CANCEL_ENDPOINT, headers, order);
    }

    @Step("Получение списка заказов")
    public Response getOrders(Map<String, String> headers, int limitOrdersPerPage) {
        return restClient.getRequest(String.format(ORDERS_GET_ENDPOINT, limitOrdersPerPage), headers);
    }
}
