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

public class E2ETests{
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
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Happy Scenario: Complete E2E flow from login to successful checkout
    @Test
    public void testSuccessfulEndToEndCheckout() throws InterruptedException {
        // Login
        login("standard_user", "secret_sauce");

        // Add product to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        // Verify product in cart
        WebElement cartItem = driver.findElement(By.className("inventory_item_name"));
        Assert.assertEquals(cartItem.getText(), "Sauce Labs Backpack", "Product not found in cart");

        // Proceed to checkout
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("ahmed");
        driver.findElement(By.id("last-name")).sendKeys("bisher");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();

        // Finish checkout
        driver.findElement(By.id("finish")).click();

        // Verify checkout complete
        Thread.sleep(1000); // Wait for confirmation page
        WebElement completeText = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(completeText.getText(), "Thank you for your order!",
                "Checkout not completed successfully");
    }

    // Negative Scenario: Attempt checkout with missing required fields
    @Test
    public void testCheckoutWithMissingFields() throws InterruptedException {
        // Login
        login("standard_user", "secret_sauce");

        // Add product to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        // Proceed to checkout
        driver.findElement(By.id("checkout")).click();

        // Submit without filling fields
        driver.findElement(By.id("continue")).click();

        // Verify error message
        Thread.sleep(1000); // Wait for error to appear
        WebElement error = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(error.getText().contains("First Name is required"),
                "Expected checkout error not displayed");
    }
}