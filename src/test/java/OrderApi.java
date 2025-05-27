// OrderApi.java
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private final String orderCreateUrl = "/api/v1/orders";
    private final String orderListUrl = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(orderCreateUrl);
    }

    @Step("Получение списка заказов")
    public Response getOrderList() {
        return given()
                .when()
                .get(orderListUrl);
    }
}