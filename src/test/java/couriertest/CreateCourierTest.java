package couriertest;

import base.BaseTest;
import courier.Courier;
import courier.CourierDataForTest;
import courier.CourierStepMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest extends BaseTest {
    private final CourierDataForTest courierDataForTest = new CourierDataForTest();
    String id = null;

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера с валидно заполненными полями")
    public void createCourier() {
        Courier courier = new Courier(courierDataForTest.getExistingLogin(), courierDataForTest.getExistingPassword(), courierDataForTest.getFirstName());
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
    public void createCourierWithoutLogin() {
        Courier courier = new Courier("", courierDataForTest.getExistingPassword(), courierDataForTest.getFirstName());
        Response response = CourierStepMethods.createCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Создание курьера только с логином и именем")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier(courierDataForTest.getExistingLogin(), "", courierDataForTest.getFirstName());
        Response response = CourierStepMethods.createCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание одинаковых курьеров")
    @Description("Создание курьера с валидными данными и повтор создания этого же курьера")
    public void createDoubleCouriers() {
        Courier courier = new Courier(courierDataForTest.getExistingLogin(), courierDataForTest.getExistingPassword(), courierDataForTest.getFirstName());
        CourierStepMethods.createCourier(courier);
        Response response = CourierStepMethods.createCourier(courier);
        id = CourierStepMethods.loginCourier(courier).then().extract().path("id").toString();
        response.then().assertThat().statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void deleteCourier() {
        if (id != null) {
            CourierStepMethods.deleteCourier(id);
        }
    }
}
