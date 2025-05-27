// OrderCreationTest.java
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest extends BaseTest {
    private final OrderApi orderApi = new OrderApi();
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

        orderApi.createOrder(order)
                .then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }
}