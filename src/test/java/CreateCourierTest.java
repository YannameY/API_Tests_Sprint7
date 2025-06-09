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

//Класс тестов для проверки создания курьеров через API
public class CreateCourierTest extends Client {
    private final CourierData courierData = new CourierData();
    String id = null;

    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера с валидно заполненными полями")
    public void createCourier () {
        Courier courier = new Courier(courierData.getExistingLogin(), courierData.getExistingPassword(), courierData.getFirstName());
        Response response = CourierStepMethods.createCourier(courier);
        id = CourierStepMethods.loginCourier(courier)
                .then().extract()
                .path("id").toString();
        response.then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Создание курьера только с паролем и именем")
    public void createCourierWithoutLogin () {
        Courier courier = new Courier("", courierData.getExistingPassword(), courierData.getFirstName());
        Response response = CourierStepMethods.createCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Создание курьера только с логином и именем")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier(courierData.getExistingLogin(), "", courierData.getFirstName());
        Response response = CourierStepMethods.createCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание одинаковых курьеров")
    @Description("Создание курьера с валидными данными и повтор создания этого же курьера")
    public void createDoubleCouriers() {
        Courier courier = new Courier(courierData.getExistingLogin(), courierData.getExistingPassword(), courierData.getFirstName());
        CourierStepMethods.createCourier(courier);
        Response response = CourierStepMethods.createCourier(courier);
        id = CourierStepMethods.loginCourier(courier).then().extract().path("id").toString();
        response.then().assertThat().statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void deleteCourier() {
        if (id != null) {
            CourierStepMethods.deleteCourier(id);
        }
    }
}