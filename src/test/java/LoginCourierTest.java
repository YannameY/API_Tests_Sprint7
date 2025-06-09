import courier.Courier;
import courier.CourierData;
import courier.CourierStepMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


//Класс тестов для проверки авторизации курьеров через API
public class LoginCourierTest extends Client {
    private final CourierData courierData = new CourierData();
    String id = null;


    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }

    @Test
    @DisplayName("Успешный логин курьера")
    @Description("Логин курьера в системе.Курьер может авторизоваться. Успешный запрос возвращает id.")
    public void loginCourier() {
        Courier courier = new Courier(courierData.getExistingLogin(), courierData.getExistingPassword());
        CourierStepMethods.createCourier(courier);
        id = CourierStepMethods.loginCourier(courier)
                .then()
                .extract()
                .path("id")
                .toString();
        Response response = CourierStepMethods.loginCourier(courier);
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Для авторизации курьера необходимо передать все обязательные поля. Передаётся пустой логин")
    public void authorizationCourierWithoutLogin() {
        Courier courier = new Courier("", courierData.getExistingPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        response.then().assertThat().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Для авторизации курьера необходимо передать все обязательные поля. Передается пустой пароль курьера")
    public void authorizationCourierWithoutPassword() {
        Courier courier = new Courier(courierData.getExistingLogin(), "");
        Response response = CourierStepMethods.loginCourier(courier);
        response.then().assertThat().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера c неправильным логином")
    @Description("Для авторизации курьера необходимо передать существующие данные. Передается неправильный логин")
    public void authorizationCourierWithNonExistentLogin() {
        Courier courier = new Courier(courierData.getNonExistLogin(), courierData.getExistingPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        response.then().assertThat().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера c неправильным паролем")
    @Description("Для авторизации курьера необходимо передать существующие данные. Передается неверный пароль курьера")
    public void authorizationCourierWithNonExistentPassword() {
        Courier courier = new Courier(courierData.getExistingLogin(), courierData.getNonExistPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        response.then().assertThat().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void deleteCourier() {
        if (id != null) {
            CourierStepMethods.deleteCourier(id);
        }
    }
}