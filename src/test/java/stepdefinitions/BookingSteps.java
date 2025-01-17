package stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookingSteps {
    private static final Logger logger = LogManager.getLogger(BookingSteps.class);
    private static final String BASE_URL = "https://restful-booker.herokuapp.com";
    private String authToken;
    private Response response;
    private int bookingId;

    @Before
    public void setup() {
        logger.debug("Token alma işlemi başlıyor");
        String requestBody = """
                {
                    "username": "admin",
                    "password": "password123"
                }""";

        authToken = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL + "/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
        logger.debug("Auth token alındı");
    }

    @Given("the user connects to the API")
    public void theUserConnectsToTheAPI() {
        logger.info("API'ye bağlanılıyor...");
    }

    @Given("a booking with ID {string} exists")
    public void a_booking_with_id_exists(String bookingId) {
        logger.info("Rezervasyon ID'si kontrol ediliyor: " + bookingId);
        given()
                .when()
                .get(BASE_URL + "/booking/" + bookingId)
                .then()
                .statusCode(200);
        logger.info("Rezervasyon ID'si doğrulandı: " + bookingId);
    }

    @Given("the user has a valid authentication token")
    public void the_user_has_a_valid_authentication_token() {
        logger.info("Geçerli bir authentication token alınıyor...");
        // Token alma işlemi burada gerçekleştirilir.
        authToken = given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"admin\", \"password\": \"password123\"}")
                .when()
                .post(BASE_URL + "/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
        logger.info("Authentication token alındı: " + authToken);
    }

    @When("the user sends a POST request to {string} with valid credentials")
    public void theUserSendsAPOSTRequestToWithValidCredentials(String endpoint) {
        logger.info("Geçerli kimlik bilgileri ile giriş yapılıyor...");
        String requestBody = """
                {
                    "username": "admin",
                    "password": "password123"
                }""";

        response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL + endpoint)
                .then()
                .extract()
                .response();
    }

    @When("the user sends a GET request to {string}")
    public void theUserSendsAGETRequestTo(String endpoint) {
        logger.info("GET isteği gönderiliyor: " + endpoint);
        response = given()
                .when()
                .get(BASE_URL + endpoint)
                .then()
                .extract()
                .response();
    }

    @When("the user sends a POST request to {string} with valid booking data")
    public void theUserSendsAPOSTRequestToWithValidBookingData(String endpoint) {
        logger.info("Yeni rezervasyon oluşturuluyor...");
        String bookingBody = """
                {
                    "firstname": "Test",
                    "lastname": "User",
                    "totalprice": 150,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2024-01-01",
                        "checkout": "2024-01-05"
                    },
                    "additionalneeds": "Breakfast"
                }""";

        response = given()
                .contentType(ContentType.JSON)
                .body(bookingBody)
                .when()
                .post(BASE_URL + endpoint)
                .then()
                .extract()
                .response();

        bookingId = response.path("bookingid");
        logger.info("Rezervasyon oluşturuldu. ID: " + bookingId);
    }

    @When("the user sends a PUT request to {string} with updated booking data")
    public void theUserSendsAPUTRequestToWithUpdatedBookingData(String endpoint) {
        logger.info("Rezervasyon güncelleniyor. ID: " + bookingId);
        String updateBody = """
                {
                    "firstname": "UpdatedName",
                    "lastname": "UpdatedSurname",
                    "totalprice": 200,
                    "depositpaid": false,
                    "bookingdates": {
                        "checkin": "2024-02-01",
                        "checkout": "2024-02-05"
                    },
                    "additionalneeds": "Dinner"
                }""";

        response = given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + authToken)
                .accept("application/json")
                .body(updateBody)
                .when()
                .put(BASE_URL + endpoint)
                .then()
                .extract()
                .response();
        logger.info("Rezervasyon güncellendi");
    }

    @When("the user sends a PATCH request to {string} with partial booking data")
    public void theUserSendsAPATCHRequestToWithPartialBookingData(String endpoint) {
        logger.info("Rezervasyon kısmen güncelleniyor. ID: " + bookingId);
        String partialUpdateBody = """
                {
                    "firstname": "PartiallyUpdatedName"
                }""";

        response = given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + authToken)
                .accept("application/json")
                .body(partialUpdateBody)
                .when()
                .patch(BASE_URL + endpoint)
                .then()
                .extract()
                .response();
        logger.info("Rezervasyon kısmen güncellendi");
    }

    @When("the user sends a DELETE request to {string}")
    public void theUserSendsADELETERequestTo(String endpoint) {
        logger.info("Rezervasyon siliniyor. ID: " + bookingId);
        response = given()
                .header("Cookie", "token=" + authToken)
                .accept("application/json")
                .when()
                .delete(BASE_URL + endpoint)
                .then()
                .extract()
                .response();
        logger.info("Rezervasyon silindi");
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        logger.info("Status code kontrol ediliyor...");
        response.then().statusCode(statusCode);
        logger.info("Status code doğrulandı: " + statusCode);
    }

    @Then("the response should contain a token")
    public void theResponseShouldContainAToken() {
        logger.info("Token kontrolü yapılıyor...");
        response.then().body("token", notNullValue());
        logger.info("Token başarıyla oluşturuldu");
    }

    @Then("the response should contain a list of booking IDs")
    public void theResponseShouldContainAListOfBookingIDs() {
        logger.info("Rezervasyon kimlikleri kontrol ediliyor...");
        response.then().body("$", not(empty()));
        logger.info("Rezervasyon kimlikleri başarıyla alındı");
    }

    @Then("the response should contain the booking details")
    public void theResponseShouldContainTheBookingDetails() {
        logger.info("Rezervasyon detayları kontrol ediliyor...");
        response.then()
                .body("firstname", notNullValue())
                .body("lastname", notNullValue());
        logger.info("Rezervasyon detayları doğrulandı");
    }

    @Then("the response should contain the booking ID")
    public void theResponseShouldContainTheBookingID() {
        logger.info("Rezervasyon ID kontrol ediliyor...");
        response.then().body("bookingid", notNullValue());
        logger.info("Rezervasyon ID başarıyla alındı");
    }

    @Then("the response should match the provided booking details")
    public void theResponseShouldMatchTheProvidedBookingDetails() {
        logger.info("Rezervasyon detayları eşleştiriliyor...");
        response.then()
                .body("booking.firstname", equalTo("Test"))
                .body("booking.lastname", equalTo("User"));
        logger.info("Rezervasyon detayları eşleştirildi");
    }

    @Then("the response should contain the updated booking details")
    public void theResponseShouldContainTheUpdatedBookingDetails() {
        logger.info("Güncellenmiş rezervasyon detayları kontrol ediliyor...");
        response.then()
                .body("firstname", equalTo("UpdatedName"))
                .body("lastname", equalTo("UpdatedSurname"));
        logger.info("Güncellenmiş rezervasyon detayları doğrulandı");
    }

    @Then("the response should contain the partially updated booking details")
    public void theResponseShouldContainThePartiallyUpdatedBookingDetails() {
        logger.info("Kısmen güncellenmiş rezervasyon detayları kontrol ediliyor...");
        response.then()
                .body("firstname", equalTo("PartiallyUpdatedName"));
        logger.info("Kısmen güncellenmiş rezervasyon detayları doğrulandı");
    }

    @Then("the response body should be {string}")
    public void theResponseBodyShouldBe(String expectedResponseBody) {
        logger.info("Response body kontrol ediliyor...");
        response.then().body(equalTo(expectedResponseBody));
        logger.info("Response body doğrulandı: " + expectedResponseBody);
    }
}