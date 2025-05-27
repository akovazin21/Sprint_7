// CourierApi.java
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {
    private final String courierLoginUrl = "/api/v1/courier/login";
    private final String courierCreateUrl = "/api/v1/courier";

    @Step("Создание курьера {courier.login}")
    public Response createCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(courierCreateUrl);
    }

    @Step("Логин курьера {courier.login}")
    public Response loginCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(courierLoginUrl);
    }

    @Step("Получение ID курьера")
    public int getCourierId(Courier courier) {
        return loginCourier(courier)
                .then()
                .extract()
                .path("id");
    }

    @Step("Удаление курьера {courierId}")
    public Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete(courierCreateUrl + "/" + courierId);
    }
}