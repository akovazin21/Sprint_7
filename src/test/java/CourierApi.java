import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class CourierApi {
    private final String courierLoginUrl = "/api/v1/courier/login";
    private final String courierCreateUrl = "/api/v1/courier";

    @Step("Создание курьера {courier.login}")
    public int createCourier(Courier courier) {
        given()
                .body(courier)
                .when()
                .post(courierCreateUrl)
                .then()
                .statusCode(SC_CREATED);

        return loginCourier(new Courier(courier.getLogin(), courier.getPassword()))
                .then()
                .extract()
                .path("id");
    }

    @Step("Логин курьера {courier.login}")
    public Response loginCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(courierLoginUrl);
    }

    @Step("Удаление курьера {courierId}")
    public void deleteCourier(int courierId) {
        given()
                .when()
                .delete(courierCreateUrl + "/" + courierId)
                .then()
                .statusCode(SC_OK);
    }
}