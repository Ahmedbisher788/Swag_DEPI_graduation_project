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

public class ProductDetailsPageTests {
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
        // Perform login to reach home page
        login("standard_user", "secret_sauce");
        // Navigate to product details page for Sauce Labs Backpack (id=4)
        driver.findElement(By.id("item_4_title_link")).click();
    }

    @AfterMethod
    void tearDown() {
        if (driver != null) {
            driver.quit(); // Properly closes the browser
        }
    }

    // Happy Scenario 1: Verify product name and price on details page
    @Test
    public void testProductDetailsDisplay() throws InterruptedException {
        // Verify product name and price
        Thread.sleep(1000); // Wait for page load
        WebElement productName = driver.findElement(By.className("inventory_details_name"));
        WebElement productPrice = driver.findElement(By.className("inventory_details_price"));
        Assert.assertEquals(productName.getText(), "Sauce Labs Backpack",
                "Incorrect product name displayed");
        Assert.assertEquals(productPrice.getText(), "$29.99",
                "Incorrect product price displayed");
    }

    // Happy Scenario 2: Add product to cart and verify cart badge
    @Test
    public void testAddProductToCart() throws InterruptedException {
        // Add product to cart
        driver.findElement(By.id("add-to-cart")).click();

        // Verify cart badge updates
        Thread.sleep(1000); // Wait for cart update
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "1", "Cart badge did not update correctly");
    }

    // Happy Scenario 3: Add and remove product from cart, verify button state
    @Test
    public void testAddAndRemoveProduct() throws InterruptedException {
        // Add product to cart
        driver.findElement(By.id("add-to-cart")).click();

        // Remove product from cart
        driver.findElement(By.id("remove")).click();

        // Verify "Add to cart" button reappears
        Thread.sleep(1000); // Wait for button update
        boolean isAddButtonPresent = driver.findElements(By.id("add-to-cart")).size() > 0;
        Assert.assertTrue(isAddButtonPresent, "Add to cart button did not reappear after removal");
    }

    // Negative Scenario 1: Add product multiple times and verify cart badge
    @Test
    public void testMultipleAddToCart() throws InterruptedException {
        // Add product to cart multiple times
        driver.findElement(By.id("add-to-cart")).click();
        Thread.sleep(500); // Short wait for first click
        boolean isAddButtonEnabled = driver.findElements(By.id("add-to-cart-sauce-labs-backpack")).size() > 0;

        // Verify cart badge does not increment beyond 1
        Thread.sleep(1000); // Wait for cart update
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "1", "Cart badge incremented incorrectly");
        Assert.assertFalse(isAddButtonEnabled, "Add to cart button should be disabled after adding");
    }
}