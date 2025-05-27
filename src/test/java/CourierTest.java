// CourierTest.java
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
            courierApi.deleteCourier(courierId)
                    .then()
                    .statusCode(SC_OK);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourierSuccess() {
        courierApi.createCourier(testCourier)
                .then()
                .statusCode(SC_CREATED)
                .body("ok", is(true));

        courierId = courierApi.loginCourier(new Courier(testCourier.getLogin(), testCourier.getPassword()))
                .then()
                .statusCode(SC_OK)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    public void testCreateDuplicateCourier() {
        courierApi.createCourier(testCourier)
                .then()
                .statusCode(SC_CREATED);

        courierApi.createCourier(testCourier)
                .then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        courierId = courierApi.loginCourier(new Courier(testCourier.getLogin(), testCourier.getPassword()))
                .then()
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void testCreateCourierWithoutLogin() {
        Courier courierWithoutLogin = new Courier(null, "password123", "Test");
        courierApi.createCourier(courierWithoutLogin)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        Courier courierWithoutPassword = new Courier(login, null, "Test");
        courierApi.createCourier(courierWithoutPassword)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}