package demo;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relevantcodes.extentreports.LogStatus;
import demo.pages.AdventureDetails;
import demo.pages.Adventures;
import demo.pages.Home;
import demo.pages.Login;
import demo.pages.Register;
import demo.pages.Reservations;
import demo.utils.Setup;
import demo.wrappers.Wrappers;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class QtripTests extends Setup {
    // use headless browser for API tests. Modify in DriverSingleton
    // comment out headless for UI/functional tests

    String baseUrl = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
    String basePath = "/api/v1";

    @Test(groups = { "api" })
    /*
     * Verify total 8 cities are in the database.
     * Verify Bengaluru is one of the cities.
     * Save the response in a json file.
     * Validate schema against expected schema
     */
    public void qtrip_non_functional_001() throws StreamWriteException, DatabindException, IOException {
        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = basePath;
        RequestSpecification http = RestAssured.given();
        Response resp = http.when().get("/cities");

        // verify status code
        Assert.assertEquals(resp.statusCode(), 200);
        Wrappers.logSuccess("Status code validated");

        // verify 8 cities
        JsonPath jp = new JsonPath(resp.getBody().asString());
        ArrayList<HashMap<String, String>> cities = jp.get();
        Assert.assertEquals(cities.size(), 8);
        Wrappers.logSuccess("8 cities found");

        // verify presence of bengaluru
        boolean hasBengaluru = false;
        for (HashMap<String, String> citiDetails : cities) {
            if (citiDetails.get("city").equals("Bengaluru")) {
                hasBengaluru = true;
                break;
            }
        }
        Assert.assertTrue(hasBengaluru, "Bengaluru is not present");
        Wrappers.logSuccess("Bengaluru city found");

        // save json and schema validation
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(Setup.resourcesFolderPath + "cities_list.json"), cities);
        File citiesSchema = new File(Setup.resourcesFolderPath + "cities_schema.json");
        JsonSchemaValidator matcher = JsonSchemaValidator.matchesJsonSchema(citiesSchema);
        resp.then().assertThat().body(matcher);
        Wrappers.logSuccess("Cities schema is valid");
    }

    @Test(groups = { "api" })
    /*
     * hit /cities with query params of different lengths for bengaluru
     * for length>=3, success 200 for valid query
     * for length<3, 404 with message
     * "City Query length should be atleast 3! Currently it is only <n>"
     * for length>3 but unknown city, 400 bad request with message ""
     */
    public void qtrip_non_functional_002() {
        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = basePath;

        // tests for valid query
        String validQueryParam = "bengaluru";
        for (int i = 1; i < validQueryParam.length(); i++) {
            Wrappers.logInfo("Testing /cities for valid query");
            Wrappers.logInfo("Current length of query params: " + i);
            RequestSpecification http = RestAssured.given().queryParam("q", validQueryParam.substring(0, i));
            Response resp = http.when().get("/cities");
            if (i < 3) {
                Assert.assertEquals(resp.statusCode(), 404);
                JsonPath jp = new JsonPath(resp.getBody().asString());
                String responseMessage = jp.getString("message");
                String actualMessage = "City Query length should be atleast 3! Currently it is only " + i;
                Assert.assertEquals(responseMessage, actualMessage);
                Wrappers.logSuccess("Status code, error message are validated for query length: " + i);
            } else {
                Assert.assertEquals(resp.statusCode(), 200);
                File citiesSchema = new File(Setup.resourcesFolderPath + "cities_schema.json");
                JsonSchemaValidator matcher = JsonSchemaValidator.matchesJsonSchema(citiesSchema);
                resp.then().assertThat().body(matcher);
                Wrappers.logSuccess("Status code, response schema are validated for query length: " + i);
            }
        }

        // tests for valid query
        String invalidQueryParam = "hyderabad";
        for (int i = 1; i < invalidQueryParam.length(); i++) {
            Wrappers.logInfo("Testing /cities for invalid query");
            Wrappers.logInfo("Current length of query params: " + i);
            RequestSpecification http = RestAssured.given().queryParam("q", invalidQueryParam.substring(0, i));
            Response resp = http.when().get("/cities");
            if (i < 3) {
                Assert.assertEquals(resp.statusCode(), 404);
                JsonPath jp = new JsonPath(resp.getBody().asString());
                String responseMessage = jp.getString("message");
                String actualMessage = "City Query length should be atleast 3! Currently it is only " + i;
                Assert.assertEquals(responseMessage, actualMessage);
                Wrappers.logSuccess("Status code, error message are validated for query length: " + i);
            } else {
                SoftAssert soft = new SoftAssert();
                soft.assertEquals(resp.statusCode(), 400);
                Setup.test.log(LogStatus.ERROR,
                        "Bug found! For unknown city parameter, expected status code is 400, but 200 found with empty list json response");
                soft.assertAll();
            }
        }

    }

    @Test(groups = { "api" })
    /*
     * register for a new user with valid email and valid password(min 6 char long).
     * status code 201
     * re-register with same username or password<6 --> 400 with appropiate message
     * 
     */
    public void qtrip_non_functional_003() throws JSONException {
        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = basePath;
        SoftAssert softAssert = new SoftAssert();

        // valid email and password
        String email = "testEmail_" + UUID.randomUUID() + "@testDomain.com";
        String password = "testPassword_" + UUID.randomUUID();
        JSONObject obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);
        RequestSpecification http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        Response resp = http.when().post("/register");
        softAssert.assertEquals(resp.statusCode(), 201);
        Wrappers.logSuccess("Registration successfull for valid username and password");

        // test with existing email
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);
        http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        resp = http.when().post("/register");
        softAssert.assertEquals(resp.statusCode(), 400);
        JsonPath jp = new JsonPath(resp.getBody().asString());
        softAssert.assertEquals(jp.getString("message"), "Email already exists");
        Wrappers.logSuccess("Status code, error message validated for existing email registration");

        // test with password<6
        email = "testEmail_" + UUID.randomUUID() + "@testDomain.com";
        password = "abc";
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);
        http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        resp = http.when().post("/register");
        softAssert.assertEquals(resp.statusCode(), 400);
        jp = new JsonPath(resp.getBody().asString());
        softAssert.assertEquals(jp.getString("message"), "Password must be atleast 6 in length");
        Wrappers.logSuccess("Status code, error message validated for password<6");

        softAssert.assertAll();
    }

    @Test(groups = { "api" })
    /*
     * register a new user with valid email, password and then do succesful login.
     * after successful login(201), save token, user id
     * verify 404 when using unregistered email
     * verify 403 when registred email with wrong password
     */
    public void qtrip_non_functional_004() throws JSONException {
        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = basePath;

        // new user registration
        Wrappers.logInfo("Performing new user registration");
        String email = "testEmail_" + UUID.randomUUID() + "@testDomain.com";
        String password = "testPassword_" + UUID.randomUUID();
        JSONObject obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);
        RequestSpecification http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        Response resp = http.when().post("/register");
        Assert.assertEquals(resp.statusCode(), 201);
        Wrappers.logSuccess("New user registration is successful");

        // existing user login
        Wrappers.logInfo("Performing existing user login");
        obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);
        http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        resp = http.when().post("/login");
        Assert.assertEquals(resp.statusCode(), 201);
        Wrappers.logSuccess("Existing user login is successful");

        // unregistred used login
        Wrappers.logInfo("Performing un-registered user login");
        obj = new JSONObject();
        obj.put("email", "unregisteredUser08650454350454504045@testDomain.com");
        obj.put("password", "unregisteredPassword57687686826868686");
        http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        resp = http.when().post("/login");
        Assert.assertEquals(resp.statusCode(), 404);
        Wrappers.logSuccess("Unregisterted user login is unsuccessful");

        // registrered used login with incorrect password
        Wrappers.logInfo("Performing registered user login with incorrect password");
        obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", "incorrectPassword");
        http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        resp = http.when().post("/login");
        Assert.assertEquals(resp.statusCode(), 403);
        Wrappers.logSuccess("Registerted user login is unsuccessful for incorrect password");
    }

    @Test(groups = { "api" })
    /*
     * register new user
     * login with details
     * book a reservation for city bengaluru and adventure Shiwood
     * book for 2 users at some future date
     */
    public void qtrip_non_functional_005() throws JSONException {
        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = basePath;

        // new user registration
        Wrappers.logInfo("Performing new user registration");
        String email = "testEmail_" + UUID.randomUUID() + "@testDomain.com";
        String password = "testPassword_" + UUID.randomUUID();
        JSONObject obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);
        RequestSpecification http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        Response resp = http.when().post("/register");
        Assert.assertEquals(resp.statusCode(), 201);
        Wrappers.logSuccess("New user registration is successful");

        // existing user login
        Wrappers.logInfo("Performing existing user login");
        obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);
        http = RestAssured.given().header("Content-Type", "application/json").body(obj.toString());
        resp = http.when().post("/login");
        Assert.assertEquals(resp.statusCode(), 201);
        Wrappers.logSuccess("Existing user login is successful");
        JsonPath jp = new JsonPath(resp.getBody().asString());
        String token = jp.getString("data.token");
        String userID = jp.getString("data.id");

        // city id
        http = RestAssured.given().queryParam("q", "bengaluru");
        resp = http.when().get("/cities");
        Assert.assertEquals(resp.statusCode(), 200);
        jp = new JsonPath(resp.getBody().asString());
        ArrayList<HashMap<String, String>> cityOutput = jp.get();
        Assert.assertFalse(cityOutput.isEmpty());
        String cityID = cityOutput.get(0).get("id");
        Wrappers.logSuccess("City id found");

        // adventure id
        http = RestAssured.given().queryParam("city", cityID);
        resp = http.when().get("/adventures");
        Assert.assertEquals(resp.statusCode(), 200);
        jp = new JsonPath(resp.getBody().asString());
        ArrayList<HashMap<String, String>> adventureOutput = jp.get();
        Assert.assertFalse(adventureOutput.isEmpty());
        boolean hasGivenAdbenture = false;
        String adventureID = null;
        for (HashMap<String, String> adventure : adventureOutput) {
            if (adventure.get("name").equals("Shiwood")) {
                hasGivenAdbenture = true;
                adventureID = adventure.get("id");
                break;
            }
        }
        Assert.assertTrue(hasGivenAdbenture);
        Wrappers.logSuccess("Adventure id found");

        // new reservation
        Wrappers.logInfo("Performing new reservation");
        obj = new JSONObject();
        obj.put("userId", userID);
        obj.put("name", "Gourab");
        obj.put("date", "2025-09-15");
        obj.put("person", 2);
        obj.put("adventure", adventureID);
        http = RestAssured.given().header("Content-Type", "application/json").header("Authorization", "Bearer " + token)
                .body(obj.toString());
        resp = http.when().post("/reservations/new");
        Assert.assertEquals(resp.statusCode(), 200);
        Wrappers.logSuccess("Reservation is successful");

        // check existing reservation status
        http = RestAssured.given().header("Authorization", "Bearer " + token).queryParams("id", userID);
        resp = http.when().get("/reservations");
        Assert.assertEquals(resp.statusCode(), 200);
        jp = new JsonPath(resp.getBody().asString());
        ArrayList<HashMap<String, String>> reservationOutput = jp.get();
        Assert.assertEquals(reservationOutput.size(), 1);
        Assert.assertFalse(jp.getBoolean("isCancelled"));
        String reservationID = reservationOutput.get(0).get("id");
        Wrappers.logSuccess("Reservation status is OK");

        // cancel reservation
        obj = new JSONObject();
        obj.put("userId", userID);
        http = RestAssured.given().header("Authorization", "Bearer " + token).header("Content-Type", "application/json")
                .body(obj.toString());
        resp = http.when().delete("/reservations/" + reservationID);
        Assert.assertEquals(resp.statusCode(), 200);
        Wrappers.logSuccess("Reservation is cancelled");

    }

    @Test(groups = { "functional" })
    /*
     * navigate to homepage
     * register for a new user
     * login with valid details
     * logout
     */
    public void qtrip_ui_001() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Home home = new Home(driver);
        Register register = new Register(driver);
        Login login = new Login(driver);

        // new user regostration
        home.navigateToHome();
        Assert.assertTrue(driver.getCurrentUrl().contains("qtrip"), "Target url is invalid");
        Wrappers.logSuccess("Homepage url is valid");
        Wrappers.clickOnElement(driver, home.homepageRegisterButton);
        Assert.assertTrue(wait.until(ExpectedConditions.urlContains("/register")));
        Wrappers.logSuccess("Landed on register page");
        String email = "testUser_" + UUID.randomUUID() + "@testDomain.com";
        String password = "testPassword" + UUID.randomUUID();
        Wrappers.enterText(driver, register.emailField, email, false);
        Wrappers.enterText(driver, register.passwordField, password, false);
        Wrappers.enterText(driver, register.confirmpasswordField, password, false);
        Wrappers.clickOnElement(driver, register.registerButton);
        Assert.assertTrue(wait.until(ExpectedConditions.urlContains("/login")), "registration failed");
        Wrappers.logSuccess("Registration is successful");

        // existing user login
        Wrappers.enterText(driver, login.emailField, email, false);
        Wrappers.enterText(driver, login.passwordField, password, false);
        Wrappers.clickOnElement(driver, login.loginButton);
        Assert.assertTrue(wait.until(ExpectedConditions.urlToBe(home.pageUrl)), "Login failed");
        Assert.assertTrue(home.homepageLogoutButton.isDisplayed(), "Login failed");
        Wrappers.logSuccess("Login is successful");

        // existing user logout
        Wrappers.clickOnElement(driver, home.homepageLogoutButton);
        Assert.assertTrue(home.homepageLoginHereButton.isDisplayed(), "Logout failed");
        Wrappers.logSuccess("Logout is successful");
    }

    @Test(groups = { "functional" })
    public void qtrip_ui_002() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Home home = new Home(driver);
        Adventures adventures = new Adventures(driver);
        home.navigateToHome();

        // search test with unknown city
        String unknownCity = "hyderabad";
        Wrappers.enterText(driver, home.homepageSearchField, unknownCity, true);
        wait.until(ExpectedConditions.visibilityOf(home.noCityFound));
        Assert.assertTrue(home.noCityFound.isDisplayed(), "City found with unknown input");
        Wrappers.logSuccess("No city found is displayed for unknown city " + unknownCity);

        // search test with known city
        String knownCity = "bengaluru";
        Wrappers.enterText(driver, home.homepageSearchField, knownCity, true);
        wait.until(ExpectedConditions.visibilityOf(home.automcompleteSearchResult));
        Assert.assertTrue(home.automcompleteSearchResult.getText().trim().equalsIgnoreCase(knownCity),
                "Error while searching with known city " + knownCity);
        Wrappers.logSuccess("Automcomplete suggestions is displayed for city " + knownCity);
        Wrappers.clickOnElement(driver, home.automcompleteSearchResult);
        Assert.assertTrue(wait.until(ExpectedConditions.urlContains("/adventures")), "Error in adventure page");
        Wrappers.logSuccess("Landed on adventure page for city " + knownCity);

        // test on duration filter
        for (WebElement filter : adventures.durationFilters) {
            String durationFilterText = filter.getText().trim();
            Wrappers.selectDurationFilter(durationFilterText, adventures.durationFilterSelect);
            Assert.assertTrue(Wrappers.verifyDurationFilter(durationFilterText, adventures.adventureCard),
                    "Duration filter is invalid: " + durationFilterText);
            Wrappers.logSuccess("Duration filter is valid: " + durationFilterText);
            Wrappers.clickOnElement(driver, adventures.durationClearButton);
        }

        // test on category filter
        for (WebElement catFilter : adventures.categoryFilters) {
            String categoryFilterText = catFilter.getText().trim();
            Wrappers.selectCategoryFilter(categoryFilterText, adventures.categoryFilterSelect);
            Assert.assertTrue(Wrappers.verifyCategoryFilter(categoryFilterText, adventures.adventureCard),
                    "Category filter is invalid: " + categoryFilterText);
            Wrappers.logSuccess("Category filter is valid: " + categoryFilterText);
            Wrappers.clickOnElement(driver, adventures.categoryClearButton);
        }

    }

    @Test(groups = { "functional" })
    /*
     * register a new user
     * login with same credentials
     * search for Bengaluru
     * select duration filter 12+ Hours
     * select category filter Cycling Routes
     * Select "Shiwood" adventure
     * book a reservation for Gourab, 16-10-2025 for 2 persons
     * validate the existence of reservation
     * cancel reservation
     * check if the reservation is cancelled
     * logout the current user
     */
    public void qtrip_ui_003() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Home home = new Home(driver);
        Register register = new Register(driver);
        Login login = new Login(driver);
        Adventures adventures = new Adventures(driver);
        AdventureDetails adventureDetails = new AdventureDetails(driver);
        Reservations reservations = new Reservations(driver);
        home.navigateToHome();

        // new user regostration
        home.navigateToHome();
        Assert.assertTrue(driver.getCurrentUrl().contains("qtrip"), "Target url is invalid");
        Wrappers.logSuccess("Homepage url is valid");
        Wrappers.clickOnElement(driver, home.homepageRegisterButton);
        Assert.assertTrue(wait.until(ExpectedConditions.urlContains("/register")));
        Wrappers.logSuccess("Landed on register page");
        String email = "testUser_" + UUID.randomUUID() + "@testDomain.com";
        String password = "testPassword" + UUID.randomUUID();
        Wrappers.enterText(driver, register.emailField, email, false);
        Wrappers.enterText(driver, register.passwordField, password, false);
        Wrappers.enterText(driver, register.confirmpasswordField, password, false);
        Wrappers.clickOnElement(driver, register.registerButton);
        Assert.assertTrue(wait.until(ExpectedConditions.urlContains("/login")), "registration failed");
        Wrappers.logSuccess("Registration is successful");

        // existing user login
        Wrappers.enterText(driver, login.emailField, email, false);
        Wrappers.enterText(driver, login.passwordField, password, false);
        Wrappers.clickOnElement(driver, login.loginButton);
        Assert.assertTrue(wait.until(ExpectedConditions.urlToBe(home.pageUrl)), "Login failed");
        Assert.assertTrue(home.homepageLogoutButton.isDisplayed(), "Login failed");
        Wrappers.logSuccess("Login is successful");

        // search test with known city
        String knownCity = "bengaluru";
        Wrappers.enterText(driver, home.homepageSearchField, knownCity, true);
        wait.until(ExpectedConditions.visibilityOf(home.automcompleteSearchResult));
        Assert.assertTrue(home.automcompleteSearchResult.getText().trim().equalsIgnoreCase(knownCity),
                "Error while searching with known city " + knownCity);
        Wrappers.logSuccess("Automcomplete suggestions is displayed for city " + knownCity);
        Wrappers.clickOnElement(driver, home.automcompleteSearchResult);
        Assert.assertTrue(wait.until(ExpectedConditions.urlContains("/adventures")), "Error in adventure page");
        Wrappers.logSuccess("Landed on adventure page for city " + knownCity);

        // select adventure
        String adventureName = "Shiwood";
        String durationFilterText = "12+ Hours";
        String categoryFilterText = "Cycling Routes";
        Wrappers.selectDurationFilter(durationFilterText, adventures.durationFilterSelect);
        Wrappers.selectCategoryFilter(categoryFilterText, adventures.categoryFilterSelect);
        Wrappers.selectAdventure(adventureName, adventures.adventureCard, driver);
        Assert.assertTrue(wait.until(ExpectedConditions.urlContains("/adventures/detail")),
                "adventure selection failed");
        Wrappers.logSuccess("Adventure selection is successful");

        // enter details in adventure details page
        Wrappers.enterText(driver, adventureDetails.nameField, "Gourab Pal", false);
        Wrappers.enterText(driver, adventureDetails.dateField, "16-10-2025", false);
        Wrappers.enterText(driver, adventureDetails.personField, "2", false);
        Wrappers.clickOnElement(driver, adventureDetails.reserveButton);
        wait.until(ExpectedConditions.visibilityOf(adventureDetails.reservationStatus));
        Assert.assertTrue(adventureDetails.reservationStatus.getText().contains("Greetings"),
                "Reservation failed-wrong status");
        Wrappers.logSuccess("Reservation is successful");

        // checking reservation status
        home.navigateToHome();
        Wrappers.clickOnElement(driver, home.homepageReservationButton);
        Assert.assertTrue(wait.until(ExpectedConditions.urlContains("/reservations")),
                "Failed to land on reservation page");
        Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(reservations.noReservationBanner)), "No reservation found");
        Assert.assertNotNull(reservations.noReservationBanner, "No valid reservation");
        Wrappers.logSuccess("Reservation status is validated");

        //cancel the reservation
        Wrappers.clickOnElement(driver, reservations.cancelReservationButton);
        wait.until(ExpectedConditions.visibilityOf(reservations.noReservationBanner));
        Assert.assertTrue(reservations.noReservationBanner.isDisplayed(), "Failed to cancel reservation");
        Wrappers.logSuccess("Reservation cancellation is validated");

        // existing user logout
        home.navigateToHome();
        Wrappers.clickOnElement(driver, home.homepageLogoutButton);
        Assert.assertTrue(home.homepageLoginHereButton.isDisplayed(), "Logout failed");
        Wrappers.logSuccess("Logout is successful");
        
    }
}
