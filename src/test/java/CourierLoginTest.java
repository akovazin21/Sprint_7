import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private final String courierLoginUrl = "/api/v1/courier/login";
    private final String courierCreateUrl = "/api/v1/courier";

    private int courierId;
    private String login = "testCourierLogin1234";
    private String password = "password123";
    private String firstName = "Test";

    @Before
    public void setUp() {
        // Создаем курьера перед тестами
        String requestBody = String.format(
                "{\"login\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                login, password, firstName);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierCreateUrl);

        courierId = getCourierId(login, password);
    }

    @After
    public void tearDown() {
        // Удаляем курьера после тестов
        given()
                .baseUri(baseUrl)
                .when()
                .delete(courierCreateUrl + "/" + courierId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void testCourierLoginSuccess() {
        String requestBody = String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierLoginUrl)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void testCourierLoginWrongPassword() {
        String requestBody = String.format("{\"login\":\"%s\",\"password\":\"wrongpass\"}", login);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierLoginUrl)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин без обязательного поля")
    public void testCourierLoginWithoutRequiredField() {
        // Без пароля
        String requestBody = String.format("{\"login\":\"%s\"}", login);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierLoginUrl)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    public void testNonExistentCourierLogin() {
        String requestBody = "{\"login\":\"nonexistent\",\"password\":\"pass\"}";

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierLoginUrl)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    private int getCourierId(String login, String password) {
        String requestBody = String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password);

        return given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierLoginUrl)
                .then()
                .extract()
                .path("id");
    }
}