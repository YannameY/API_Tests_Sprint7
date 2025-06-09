package ordertest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import order.OrderStepMethods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static constants.ApiConstants.SCOOTER_URL;

public class GetOrderListTest {
    private OrderStepMethods orderStepMethods;

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
        orderStepMethods = new OrderStepMethods();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в тело ответа возвращается список заказов.")
    public void getOrderList() {
        ValidatableResponse responseCreate = orderStepMethods.getOrderList();
        int actualStatusCodeCreate = responseCreate.extract().statusCode();
        List<HashMap> orderBody = responseCreate.extract().path("orders");
        Assert.assertEquals(200, actualStatusCodeCreate);
        Assert.assertNotNull(orderBody);
    }
}