package ordertest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderStepMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static constants.ApiConstants.SCOOTER_URL;
import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    private static final String firstName = "Ксения";
    private static final String lastName = "Понпон";
    private static final String address = "майями";
    private static final String metroStation = "Cape Florida Lighthouse, 1200 Crandon Blvd, Key Biscayne, FL 33149, Соединенные Штаты";
    private static final String phone = "+79993331122";
    private static final int rentTime = 6;
    private static final String deliveryDate = "2025-06-07";
    private static final String comment = "мне надо срочно!";
    private final String color;
    String track;

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    public CreateOrderTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "colour = ''{0}''")
    public static Object[] getColour() {
        return new Object[][]{
                {"BLACK"},
                {"GREY"},
                {"BLACK, GREY"},
                {""}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Заказ можно создать с указанием только одного цвета или обоих цветов")
    public void createOrder() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{color});
        Response response = OrderStepMethods.createOrder(order);
        track = response.then().extract().path("track").toString();
        response.then().assertThat().statusCode(201).and().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без указания параметра color")
    @Description("Заказ можно создать, если не указать параметр color")
    public void createOrderWithoutColor() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);
        Response response = OrderStepMethods.createOrder(order);
        track = response.then().extract().path("track").toString();
        response.then().assertThat().statusCode(201).and().assertThat().body("track", notNullValue());
    }

    @After
    public void cancelOrder() {
        OrderStepMethods.cancelOrder(track);
    }
}