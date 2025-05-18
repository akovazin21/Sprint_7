import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private final String orderCreateUrl = "/api/v1/orders";

    private final String color;

    public OrderCreationTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] getColorData() {
        return new Object[][] {
                {"BLACK"},
                {"GREY"},
                {"BLACK,GREY"},
                {""}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void testCreateOrderWithDifferentColors() {
        String requestBody = String.format(
                "{\"firstName\":\"Тест\",\"lastName\":\"Тестов\",\"address\":\"Москва\",\"metroStation\":4," +
                        "\"phone\":\"+79999999999\",\"rentTime\":5,\"deliveryDate\":\"2023-06-06\",\"comment\":\"Тестовый заказ\"," +
                        "\"color\":[\"%s\"]}", color);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(orderCreateUrl)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}