package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static constants.ApiConstants.COURIER;
import static constants.ApiConstants.LOGIN;
import static io.restassured.RestAssured.given;

public class CourierStepMethods {

    @Step("Создание курьера")
    public static Response createCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(COURIER);
    }

    @Step("Авторизация курьера")
    public static Response loginCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(LOGIN);
    }

    @Step("Удаление курьера с id: {courierId}")
    public static Response deleteCourier(String courierId) {
        return given()
                .delete(COURIER + "{courierId}", courierId);
    }
}
