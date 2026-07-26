package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.DataDriven;

public class InventoryTest extends BaseTest {

    LoginPage loginPage;
    InventoryPage inventoryPage;

    @Test
    public void verifyInventoryPageElements() {

        JsonNode data = DataDriven.jsonReader();

        String username = data.get("validUser").get("username").asText();
        String password = data.get("validUser").get("password").asText();

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);

        loginPage.login(username, password);

        Assert.assertEquals(driver.getTitle(), "Swag Labs");

        Assert.assertTrue(inventoryPage.isCartDisplayed());

        Assert.assertEquals(inventoryPage.getProductsCount(), 6);
    }
}