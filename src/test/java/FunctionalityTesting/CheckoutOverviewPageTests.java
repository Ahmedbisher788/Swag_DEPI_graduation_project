package FunctionalityTesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutOverviewPageTests {
    private WebDriver driver;
    private void login(String username, String password) {
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Disable password leak detection
        options.addArguments("--disable-features=PasswordLeakDetection");
        // Disable password manager re-authentication
        options.addArguments("--disable-password-manager-reauthentication");
        // Run in incognito mode (optional)
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, java.util.concurrent.TimeUnit.SECONDS);
        driver.get("https://www.saucedemo.com/");
        // Perform login, add product, and complete checkout info to reach checkout overview page
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("Ahmed");
        driver.findElement(By.id("last-name")).sendKeys("bisher");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Happy Scenario 1: Verify product and total price on checkout overview page
    @Test
    public void testProductAndPriceDisplay() throws InterruptedException {
        // Verify product and total price
        Thread.sleep(1000); // Wait for page load
        WebElement cartItem = driver.findElement(By.className("inventory_item_name"));
        WebElement totalPrice = driver.findElement(By.className("summary_total_label"));
        Assert.assertEquals(cartItem.getText(), "Sauce Labs Backpack",
                "Incorrect product displayed on overview page");
        Assert.assertTrue(totalPrice.getText().contains("Total: $32.39"),
                "Incorrect total price displayed");
    }

    // Happy Scenario 2: Add multiple products and verify item count
    @Test
    public void testMultipleProductsInOverview() throws InterruptedException {
        // Add another product before proceeding to overview
        driver.get("https://www.saucedemo.com/inventory.html");
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("ahmed");
        driver.findElement(By.id("last-name")).sendKeys("bisher");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();

        // Verify number of items
        Thread.sleep(1000); // Wait for page load
        WebElement cartList = driver.findElement(By.className("cart_list"));
        int itemCount = cartList.findElements(By.className("cart_item")).size();
        Assert.assertEquals(itemCount, 2, "Incorrect number of items on overview page");
    }

    // Happy Scenario 3: Finish checkout and verify navigation to completion page
    @Test
    public void testFinishCheckout() throws InterruptedException {
        // Finish checkout
        driver.findElement(By.id("finish")).click();

        // Verify navigation to completion page
        Thread.sleep(1000); // Wait for page load
        WebElement completeHeader = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(completeHeader.getText(), "Thank you for your order!",
                "Did not navigate to checkout completion page");
    }

    // Negative Scenario 1: Cancel checkout and verify return to home page
    @Test
    public void testCancelCheckout() throws InterruptedException {
        // Cancel checkout
        driver.findElement(By.id("cancel")).click();

        // Verify return to home page
        Thread.sleep(1000); // Wait for page load
        WebElement productsTitle = driver.findElement(By.className("title"));
        Assert.assertEquals(productsTitle.getText(), "Products",
                "Did not return to home page after cancel");
    }

    // Negative Scenario 2: Attempt to remove non-existent item
    @Test
    public void testRemoveNonExistentItem() throws InterruptedException {
        // Attempt to remove a non-existent item
        boolean isNonExistentButtonPresent = driver.findElements(By.id("remove-non-existent-item")).size() > 0;

        // Verify cart contents unchanged
        Thread.sleep(1000); // Wait for any potential update
        WebElement cartItem = driver.findElement(By.className("inventory_item_name"));
        Assert.assertEquals(cartItem.getText(), "Sauce Labs Backpack",
                "Cart contents changed unexpectedly");
        Assert.assertFalse(isNonExistentButtonPresent, "Non-existent remove button should not be present");
    }
}