import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;

import static org.apache.http.HttpStatus.*;

public class BaseTest {
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 30000)
                        .setParam("http.socket.timeout", 30000));
    }
}