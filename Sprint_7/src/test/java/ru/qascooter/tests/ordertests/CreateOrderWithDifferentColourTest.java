package ru.qascooter.tests.ordertests;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.qascooter.api.OrderApi;
import ru.qascooter.core.BaseTest;
import ru.qascooter.dto.order.CancelOrder;
import ru.qascooter.dto.order.CreateOrder;

import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasKey;


@RunWith(Parameterized.class)
public class CreateOrderWithDifferentColourTest extends BaseTest {

    private OrderApi orderApi;
    private final CreateOrder testOrder;
    private String track;
    private Map<String, String> headers;

    public CreateOrderWithDifferentColourTest(CreateOrder testOrder) {
        this.testOrder = testOrder;
    }

    @Parameterized.Parameters(name = "Набор тестовых данных: {0}")
    public static Object[][] getTestData() {
        Faker faker = new Faker();
        return new Object[][]{
                {new CreateOrder(faker.name().firstName(), faker.name().lastName(), faker.address().fullAddress(),
                        "12", faker.phoneNumber().phoneNumber(), 1, "2024-12-11",
                        "Test comment1", List.of("BLACK"))},
                {new CreateOrder(faker.name().firstName(), faker.name().lastName(), faker.address().fullAddress(),
                        "12", faker.phoneNumber().phoneNumber(), 2, "2024-12-11",
                        "Test comment2", List.of("GREY"))},
                {new CreateOrder(faker.name().firstName(), faker.name().lastName(), faker.address().fullAddress(),
                        "12", faker.phoneNumber().phoneNumber(), 3, "2024-12-11",
                        "Test comment3", List.of("BLACK", "GREY"))},
                {new CreateOrder(faker.name().firstName(), faker.name().lastName(), faker.address().fullAddress(),
                        "12", faker.phoneNumber().phoneNumber(), 4, "2024-12-11",
                        "Test comment4", null)}
        };
    }

    @Override
    public void setUp() {
        super.setUp();
        orderApi = new OrderApi(restClient);
        headers = Map.of("Content-Type", "application/json");
    }

    @Test
    public void testCreateOrderWithDifferentColoursSuccess() {
        Response Createresponse = orderApi.createOrder(headers, testOrder);
        Createresponse.then()
                .statusCode(SC_CREATED)
                .and()
                .body("$", hasKey("track"))
                .and()
                .body("track", is(notNullValue()));

        track = Createresponse.jsonPath().getString("track");
    }

    @After
    public void tearDown() {
        if (track != null) {
            CancelOrder cancelOrderBody = new CancelOrder(track);
            Response cancelOrderResponse = orderApi.cancelOrder(headers, cancelOrderBody);
            cancelOrderResponse.then()
                    .statusCode(SC_OK)
                    .and()
                    .body("$", hasKey("ok"))
                    .and()
                    .body("ok", is(true));
        }
    }
}
