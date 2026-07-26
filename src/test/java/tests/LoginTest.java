package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.DataDriven;

public class LoginTest extends BaseTest {

    LoginPage loginPage;

    @Test
    public void verifySuccessfulLogin() {

        JsonNode data = DataDriven.jsonReader();

        String username = data.get("validUser").get("username").asText();
        String password = data.get("validUser").get("password").asText();

        loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @Test
    public void verifyInvalidLogin() {

        JsonNode data = DataDriven.jsonReader();

        String username = data.get("invalidUser").get("username").asText();
        String password = data.get("invalidUser").get("password").asText();

        loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));
    }

    @Test
    public void verifyLoginWithoutPassword() {

        JsonNode data = DataDriven.jsonReader();

        String username = data.get("emptyPassword").get("username").asText();
        String password = data.get("emptyPassword").get("password").asText();

        loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"));
    }
}