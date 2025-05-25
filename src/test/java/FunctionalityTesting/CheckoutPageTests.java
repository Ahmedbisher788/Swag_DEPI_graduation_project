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

public class CheckoutPageTests {
    private WebDriver driver;
    private void login(String username, String password) {
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    @BeforeMethod
    public void setUp()  {
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
        // Perform login to reach home page and add product to reach checkout information page
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Happy Scenario 1: Fill all fields and verify navigation to checkout overview
    @Test
    public void testCompleteCheckoutInfo() throws InterruptedException {
        // Fill in checkout information
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();

        // Verify navigation to checkout overview page
        Thread.sleep(1000); // Wait for page load
        WebElement overviewTitle = driver.findElement(By.className("title"));
        Assert.assertEquals(overviewTitle.getText(), "Checkout: Overview",
                "Did not navigate to checkout overview page");
    }

    // Happy Scenario 2: Fill minimum required fields and verify continuation
    @Test
    public void testMinimumCheckoutInfo() throws InterruptedException {
        // Fill in minimum required checkout information
        driver.findElement(By.id("first-name")).sendKeys("Jane");
        driver.findElement(By.id("last-name")).sendKeys("Smith");
        driver.findElement(By.id("postal-code")).sendKeys("67890");
        driver.findElement(By.id("continue")).click();

        // Verify navigation to checkout overview page
        Thread.sleep(1000); // Wait for page load
        boolean isOverviewPage = driver.findElements(By.className("summary_info")).size() > 0;
        Assert.assertTrue(isOverviewPage, "Did not navigate to checkout overview page");
    }

    // Happy Scenario 3: Cancel checkout and verify return to your cart page
    @Test
    public void testCancelCheckout() throws InterruptedException {
        // Fill in fields and cancel
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("cancel")).click();

        // Verify return to your cart page
        Thread.sleep(1000); // Wait for page load
        WebElement YourCartTitle = driver.findElement(By.className("title"));
        Assert.assertEquals(YourCartTitle.getText(), "Your Cart",
                "Did not navigate to Your Cart page");
    }

    // Negative Scenario 1: Submit without filling any fields
    @Test
    public void testEmptyFieldsSubmission() throws InterruptedException {
        // Submit without filling fields
        driver.findElement(By.id("continue")).click();

        // Verify error message
        Thread.sleep(1000); // Wait for error to appear
        WebElement error = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(error.getText().contains("First Name is required"),
                "Expected empty fields error not displayed");
    }

    // Negative Scenario 2: Submit with missing postal code
    @Test
    public void testMissingPostalCode() throws InterruptedException {
        // Fill partial fields (missing postal code)
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("continue")).click();

        // Verify error message
        Thread.sleep(1000); // Wait for error to appear
        WebElement error = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(error.getText().contains("Postal Code is required"),
                "Expected missing postal code error not displayed");
    }
}