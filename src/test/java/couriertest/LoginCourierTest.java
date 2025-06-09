package couriertest;

import courier.Courier;
import courier.CourierDataForTest;
import courier.CourierStepMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static constants.ApiConstants.SCOOTER_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    private final CourierDataForTest courierDataForTest = new CourierDataForTest();
    private String id = null;
    private Courier testCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
        testCourier = new Courier(courierDataForTest.getExistingLogin(), courierDataForTest.getExistingPassword());
        Response createResponse = CourierStepMethods.createCourier(testCourier);
        createResponse.then().statusCode(201);
        id = CourierStepMethods.loginCourier(testCourier)
                .then()
                .extract()
                .path("id")
                .toString();
    }

    @Test
    @DisplayName("Успешный логин курьера")
    @Description("Логин курьера в системе. Успешный запрос возвращает id.")
    public void loginCourier() {
        Response response = CourierStepMethods.loginCourier(testCourier);
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Для авторизации курьера необходимо передать все обязательные поля. Передаётся пустой логин")
    public void authorizationCourierWithoutLogin() {
        Courier courier = new Courier("", testCourier.getPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Для авторизации курьера необходимо передать все обязательные поля. Передается пустой пароль курьера")
    public void authorizationCourierWithoutPassword() {
        Courier courier = new Courier(testCourier.getLogin(), "");
        Response response = CourierStepMethods.loginCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера c неправильным логином")
    @Description("Для авторизации курьера необходимо передать существующие данные. Передается неправильный логин")
    public void authorizationCourierWithNonExistentLogin() {
        Courier courier = new Courier(courierDataForTest.getNonExistLogin(), testCourier.getPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера c неправильным паролем")
    @Description("Для авторизации курьера необходимо передать существующие данные. Передается неверный пароль курьера")
    public void authorizationCourierWithNonExistentPassword() {
        Courier courier = new Courier(testCourier.getLogin(), courierDataForTest.getNonExistPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        if (id != null) {
            CourierStepMethods.deleteCourier(id);
        }
    }
}
