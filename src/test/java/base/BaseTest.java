package base;

import io.restassured.RestAssured;
import org.junit.Before;

import static constants.ApiConstants.SCOOTER_URL;

public class BaseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }
}
