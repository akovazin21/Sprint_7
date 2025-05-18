import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static org.hamcrest.Matchers.*;

public class CourierTest {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private final String courierCreateUrl = "/api/v1/courier";
    private final String courierLoginUrl = "/api/v1/courier/login";

    private int courierId;
    private String login = "testCourier" + System.currentTimeMillis();
    private String password = "password123";
    private String firstName = "Test";

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(newConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
                .build();

        // Установка таймаутов
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 30000)
                        .setParam("http.socket.timeout", 30000));
    }

    @After
    public void tearDown() {
        // Удаление курьера после теста
        if (courierId != 0) {
            given()
                    .baseUri(baseUrl)
                    .when()
                    .delete(courierCreateUrl + "/" + courierId)
                    .then()
                    .statusCode(200);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourierSuccess() {
        String requestBody = String.format(
                "{\"login\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                login, password, firstName);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierCreateUrl)
                .then()
                .statusCode(201)
                .body("ok", is(true));

        // Получаем ID для удаления
        courierId = getCourierId(login, password);
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    public void testCreateDuplicateCourier() {
        // Сначала создаем курьера
        createTestCourier(login, password, firstName);

        // Пытаемся создать такого же
        String requestBody = String.format(
                "{\"login\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                login, password, firstName);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierCreateUrl)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без обязательного поля")
    public void testCreateCourierWithoutRequiredField() {
        // Без логина
        String requestBody = String.format("{\"password\":\"%s\",\"firstName\":\"%s\"}", password, firstName);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierCreateUrl)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // Вспомогательные методы
    private void createTestCourier(String login, String password, String firstName) {
        String requestBody = String.format(
                "{\"login\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                login, password, firstName);

        given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(courierCreateUrl);

        this.courierId = getCourierId(login, password);
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
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }
}