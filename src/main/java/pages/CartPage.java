package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class CartPage {

    private WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // Cart Locators

    private By cartItems =
            By.className("cart_item");

    private By itemNames =
            By.className("inventory_item_name");

    private By checkoutButton =
            By.id("checkout");

    private By continueShoppingButton =
            By.id("continue-shopping");

    // Checkout Step One

    private By firstName =
            By.id("first-name");

    private By lastName =
            By.id("last-name");

    private By postalCode =
            By.id("postal-code");

    private By continueCheckoutButton =
            By.id("continue");

    // Checkout Step Two

    private By itemTotal =
            By.className("summary_subtotal_label");

    // Cart Methods

    public boolean isCartEmpty() {
        return driver.findElements(cartItems).isEmpty();
    }

    public List<String> getCartItemNames() {

        List<String> names = new ArrayList<>();

        driver.findElements(itemNames).forEach(
                item -> names.add(item.getText())
        );

        return names;
    }

    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
    }

    public void clickContinueShopping() {
        driver.findElement(continueShoppingButton).click();
    }

    public void removeProduct(String productName) {

        By removeButton = By.xpath(
                "//div[text()='" + productName +
                        "']/ancestor::div[@class='cart_item']//button"
        );

        driver.findElement(removeButton).click();
    }

    public boolean isProductInCart(String productName) {

        return getCartItemNames().contains(productName);
    }

    // Checkout Methods

    public void enterCheckoutInformation(
            String firstNameValue,
            String lastNameValue,
            String postalCodeValue) {

        driver.findElement(firstName)
                .sendKeys(firstNameValue);

        driver.findElement(lastName)
                .sendKeys(lastNameValue);

        driver.findElement(postalCode)
                .sendKeys(postalCodeValue);
    }

    public void continueCheckout() {
        driver.findElement(continueCheckoutButton).click();
    }

    public double getItemTotal() {

        String total = driver.findElement(itemTotal)
                .getText()
                .replace("Item total: $", "")
                .trim();

        return Double.parseDouble(total);
    }
}