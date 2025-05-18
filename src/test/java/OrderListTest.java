import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderListTest {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private final String orderListUrl = "/api/v1/orders";

    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrderList() {
        given()
                .baseUri(baseUrl)
                .when()
                .get(orderListUrl)
                .then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));
    }
}