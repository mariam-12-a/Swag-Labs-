package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.DataDriven;

import java.util.ArrayList;
import java.util.List;

public class CartTest extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    // Reusable Login Method

    private void loginWithValidUser() {

        JsonNode data = DataDriven.jsonReader();

        String username =
                data.get("validUser")
                        .get("username")
                        .asText();

        String password =
                data.get("validUser")
                        .get("password")
                        .asText();

        loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        inventoryPage = new InventoryPage(driver);
    }

    // =========================================================
    // Scenario 1 - Verify Social Links
    // =========================================================

    @Test
    public void verifySocialLinks() {

        loginWithValidUser();

        String originalWindow =
                driver.getWindowHandle();

        // LinkedIn

        inventoryPage.clickLinkedIn();

        inventoryPage.switchToNewTab(originalWindow);

        Assert.assertTrue(
                driver.getCurrentUrl().toLowerCase().contains("linkedin"),
                "LinkedIn URL was not opened correctly."
        );

        driver.close();

        inventoryPage.switchToWindow(originalWindow);

        // Facebook

        inventoryPage.clickFacebook();

        inventoryPage.switchToNewTab(originalWindow);

        Assert.assertTrue(
                driver.getCurrentUrl().toLowerCase().contains("facebook"),
                "Facebook URL was not opened correctly."
        );

        driver.close();

        inventoryPage.switchToWindow(originalWindow);

        // X / Twitter

        inventoryPage.clickTwitter();

        inventoryPage.switchToNewTab(originalWindow);

        Assert.assertTrue(
                driver.getCurrentUrl().toLowerCase().contains("x.com"),
                "X/Twitter URL was not opened correctly."
        );
    }

    // =========================================================
    // Scenario 2 - Verify Cart Is Empty
    // =========================================================

    @Test
    public void verifyCartIsEmpty() {

        loginWithValidUser();

        inventoryPage.openCart();

        cartPage = new CartPage(driver);

        Assert.assertTrue(
                cartPage.isCartEmpty(),
                "Cart is not empty after login."
        );
    }

    // =========================================================
    // Scenario 3 - Add 3 Specific Products
    // Data Driven from JSON
    // =========================================================

    @Test
    public void verifyAddThreeProducts() {

        JsonNode data = DataDriven.jsonReader();

        loginWithValidUser();

        List<String> expectedProducts =
                new ArrayList<>();

        for (JsonNode product :
                data.get("cartProducts")) {

            String productName =
                    product.asText();

            expectedProducts.add(productName);

            inventoryPage.addProduct(productName);
        }

        inventoryPage.openCart();

        cartPage = new CartPage(driver);

        List<String> actualProducts =
                cartPage.getCartItemNames();

        Assert.assertEquals(
                actualProducts,
                expectedProducts,
                "Cart products or their order is incorrect."
        );
    }

    // =========================================================
    // Scenario 4 - Remove One Product
    // =========================================================

    @Test
    public void verifyRemoveOneProduct() {

        JsonNode data = DataDriven.jsonReader();

        loginWithValidUser();

        List<String> products =
                new ArrayList<>();

        for (JsonNode product :
                data.get("cartProducts")) {

            String productName =
                    product.asText();

            products.add(productName);

            inventoryPage.addProduct(productName);
        }

        inventoryPage.openCart();

        cartPage = new CartPage(driver);

        String productToRemove =
                "Sauce Labs Bolt T-Shirt";

        cartPage.removeProduct(productToRemove);

        Assert.assertFalse(
                cartPage.isProductInCart(productToRemove),
                "Removed product is still present in the cart."
        );

        cartPage.clickContinueShopping();

        Assert.assertEquals(
                inventoryPage.getButtonText(productToRemove),
                "Add to cart",
                "Removed product button did not change to Add to cart."
        );

        Assert.assertEquals(
                inventoryPage.getButtonText("Sauce Labs Backpack"),
                "Remove",
                "Backpack button should still be Remove."
        );

        Assert.assertEquals(
                inventoryPage.getButtonText("Sauce Labs Onesie"),
                "Remove",
                "Onesie button should still be Remove."
        );
    }

    // =========================================================
    // Scenario 5 - Verify Cart Total Price
    // =========================================================

    @Test
    public void verifyCartTotalPrice() {

        JsonNode data = DataDriven.jsonReader();

        loginWithValidUser();

        double expectedTotal = 0.0;

        for (JsonNode product :
                data.get("cartProducts")) {

            String productName =
                    product.asText();

            double price =
                    inventoryPage.getProductPrice(productName);

            expectedTotal += price;

            inventoryPage.addProduct(productName);
        }

        inventoryPage.openCart();

        cartPage = new CartPage(driver);

        cartPage.clickCheckout();

        // Fill checkout information

        cartPage.enterCheckoutInformation(
                "Mariam",
                "Test",
                "12345"
        );

        cartPage.continueCheckout();

        double actualTotal =
                cartPage.getItemTotal();

        Assert.assertEquals(
                actualTotal,
                expectedTotal,
                0.001,
                "Calculated item total does not match checkout Item Total."
        );
    }

    // =========================================================
    // Scenario 6 - Checkout With Empty Cart
    // =========================================================

    @Test
    public void verifyCheckoutWithEmptyCart() {

        loginWithValidUser();

        inventoryPage.openCart();

        cartPage = new CartPage(driver);

        Assert.assertTrue(
                cartPage.isCartEmpty(),
                "Cart should be empty before checkout."
        );

        cartPage.clickCheckout();

        /*
         * Actual SauceDemo behavior:
         * clicking Checkout from an empty cart navigates
         * to Checkout: Your Information rather than
         * blocking the click at the cart page.
         */

        Assert.assertTrue(
                driver.getCurrentUrl()
                        .contains("checkout-step-one"),
                "Checkout did not open from the empty cart."
        );
    }

    // =========================================================
    // Scenario 7 - Cart State After Logout/Login
    // =========================================================

    @Test
    public void verifyCartStateAfterLogoutLogin() {

        JsonNode data = DataDriven.jsonReader();

        loginWithValidUser();

        List<String> expectedProducts =
                new ArrayList<>();

        int productCount = 0;

        for (JsonNode product :
                data.get("cartProducts")) {

            if (productCount == 2) {
                break;
            }

            String productName =
                    product.asText();

            expectedProducts.add(productName);

            inventoryPage.addProduct(productName);

            productCount++;
        }

        // Logout

        inventoryPage.logout();

        Assert.assertTrue(
                driver.getCurrentUrl().equals("https://www.saucedemo.com/"),
                "User was not redirected to login page after logout."
        );

        // Login again

        loginWithValidUser();

        inventoryPage.openCart();

        cartPage = new CartPage(driver);

        List<String> actualProducts =
                cartPage.getCartItemNames();

        /*
         * SauceDemo keeps the cart state after logout/login
         * for the same browser session.
         */

        Assert.assertEquals(
                actualProducts,
                expectedProducts,
                "Cart state was not preserved after logout/login."
        );
    }
}