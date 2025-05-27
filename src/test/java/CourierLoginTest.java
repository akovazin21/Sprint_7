// CourierLoginTest.java
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierLoginTest extends BaseTest {
    private final Courier validCourier = new Courier("testCourierLogin1234", "password123", "Test");
    private final CourierApi courierApi = new CourierApi();
    private int courierId;

    @Before
    public void setUp() {
        courierApi.createCourier(validCourier)
                .then()
                .statusCode(SC_CREATED);
        courierId = courierApi.getCourierId(validCourier);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId)
                    .then()
                    .statusCode(SC_OK);
        }
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void testCourierLoginSuccess() {
        int id = courierApi.loginCourier(validCourier)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("id");

        assertThat(id, notNullValue());
        assertThat(id, equalTo(courierId));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void testCourierLoginWrongPassword() {
        courierApi.loginCourier(new Courier(validCourier.getLogin(), "wrongpass"))
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин без обязательного поля")
    public void testCourierLoginWithoutRequiredField() {
        courierApi.loginCourier(new Courier(validCourier.getLogin(), null))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    public void testNonExistentCourierLogin() {
        courierApi.loginCourier(new Courier("nonexistent", "pass"))
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}