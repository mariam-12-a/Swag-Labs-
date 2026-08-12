package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class InventoryPage {

    private WebDriver driver;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private By cartIcon = By.className("shopping_cart_link");
    private By products = By.className("inventory_item");
    private By pageTitle = By.className("title");
    private By cartBadge = By.className("shopping_cart_badge");

    private By socialLinkedIn =
            By.cssSelector("a[data-test='social-linkedin']");

    private By socialFacebook =
            By.cssSelector("a[data-test='social-facebook']");

    private By socialTwitter =
            By.cssSelector("a[data-test='social-twitter']");

    // Basic Inventory Actions

    public boolean isCartDisplayed() {
        return driver.findElement(cartIcon).isDisplayed();
    }

    public int getProductsCount() {
        List<?> productList = driver.findElements(products);
        return productList.size();
    }

    public String getPageTitle() {
        return driver.findElement(pageTitle).getText();
    }

    public void openCart() {
        driver.findElement(cartIcon).click();
    }

    public int getCartCount() {

        if (driver.findElements(cartBadge).isEmpty()) {
            return 0;
        }

        return Integer.parseInt(
                driver.findElement(cartBadge).getText()
        );
    }

    // Product Actions

    public void addProduct(String productName) {

        By addButton = By.xpath(
                "//div[text()='" + productName +
                        "']/ancestor::div[@class='inventory_item']//button"
        );

        driver.findElement(addButton).click();
    }

    public void removeProduct(String productName) {

        By removeButton = By.xpath(
                "//div[text()='" + productName +
                        "']/ancestor::div[@class='inventory_item']//button"
        );

        driver.findElement(removeButton).click();
    }

    public double getProductPrice(String productName) {

        String price = driver.findElement(
                By.xpath(
                        "//div[text()='" + productName +
                                "']/ancestor::div[@class='inventory_item']" +
                                "//div[@class='inventory_item_price']"
                )
        ).getText();

        return Double.parseDouble(
                price.replace("$", "")
        );
    }

    public String getButtonText(String productName) {

        return driver.findElement(
                By.xpath(
                        "//div[text()='" + productName +
                                "']/ancestor::div[@class='inventory_item']//button"
                )
        ).getText();
    }

    // Social Links

    public void clickLinkedIn() {
        driver.findElement(socialLinkedIn).click();
    }

    public void clickFacebook() {
        driver.findElement(socialFacebook).click();
    }

    public void clickTwitter() {
        driver.findElement(socialTwitter).click();
    }

    // Window Handling

    public void switchToNewTab(String originalWindow) {

        for (String window : driver.getWindowHandles()) {

            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                return;
            }
        }
    }

    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    // Product Details

    public void openProduct(String productName) {

        driver.findElement(
                By.xpath("//div[text()='" + productName + "']")
        ).click();
    }

    // Logout

    public void logout() {

        driver.findElement(
                By.id("react-burger-menu-btn")
        ).click();

        driver.findElement(
                By.id("logout_sidebar_link")
        ).click();
    }
}