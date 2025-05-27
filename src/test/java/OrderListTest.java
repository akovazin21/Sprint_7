// OrderListTest.java
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderListTest extends BaseTest {
    private final OrderApi orderApi = new OrderApi();

    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrderList() {
        orderApi.getOrderList()
                .then()
                .statusCode(SC_OK)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));
    }
}