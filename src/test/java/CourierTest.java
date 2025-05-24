import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CourierTest extends BaseTest {
    private final CourierApi courierApi = new CourierApi();
    private int courierId;
    private final String login = "testCourier" + System.currentTimeMillis();
    private final Courier testCourier = new Courier(login, "password123", "Test");

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourierSuccess() {
        courierApi.createCourier(testCourier);
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    public void testCreateDuplicateCourier() {
        courierApi.createCourier(testCourier);

        courierApi.loginCourier(new Courier(testCourier.getLogin(), testCourier.getPassword()))
                .then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без обязательного поля")
    public void testCreateCourierWithoutRequiredField() {
        courierApi.loginCourier(new Courier(null, testCourier.getPassword()))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        courierApi.loginCourier(new Courier(testCourier.getLogin(), null))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}