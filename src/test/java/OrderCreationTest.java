import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private final String orderCreateUrl = "/api/v1/orders";

    private final String[] colors;

    public OrderCreationTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] getColorData() {
        return new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void testCreateOrderWithDifferentColors() {
        Order order = new Order(
                "Тест",
                "Тестов",
                "Москва",
                4,
                "+79999999999",
                5,
                "2023-06-06",
                "Тестовый заказ",
                List.of(colors)
        );

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(order) // Сериализация объекта в JSON автоматически
                .when()
                .post(orderCreateUrl)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}