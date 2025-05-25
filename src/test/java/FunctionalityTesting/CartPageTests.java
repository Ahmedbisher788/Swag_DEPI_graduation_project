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


public class CartPageTests {
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
    }
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Happy Scenario 1: Add product and verify it appears in cart
    @Test
    public void testProductInCart() throws InterruptedException {
        // Add product to cart and navigate to cart page
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        // Verify product is in cart
        Thread.sleep(1000); // Wait for cart page load
        WebElement cartItem = driver.findElement(By.className("inventory_item_name"));
        Assert.assertEquals(cartItem.getText(), "Sauce Labs Backpack",
                "Product not found in cart");
    }

    // Happy Scenario 2: Add multiple products and verify cart item count
    @Test
    public void testMultipleProductsInCart() throws InterruptedException {
        // Add multiple products to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        // Verify number of items in cart
        Thread.sleep(1000); // Wait for cart page load
        WebElement cartList = driver.findElement(By.className("cart_list"));
        int itemCount = cartList.findElements(By.className("cart_item")).size();
        Assert.assertEquals(itemCount, 2, "Incorrect number of items in cart");
    }

    // Happy Scenario 3: Remove product from cart and verify cart is empty
    @Test
    public void testRemoveProductFromCart() throws InterruptedException {
        // Add product to cart and navigate to cart page
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        // Remove product from cart
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        // Verify cart is empty
        Thread.sleep(1000); // Wait for cart update
        boolean isCartEmpty = driver.findElements(By.className("cart_item")).isEmpty();
        Assert.assertTrue(isCartEmpty, "Cart is not empty after removing product");
    }

    // Negative Scenario 1: Navigate to cart without adding products
    @Test
    public void testEmptyCart() throws InterruptedException {
        // Navigate to cart page without adding products
        driver.findElement(By.className("shopping_cart_link")).click();

        // Verify cart is empty
        Thread.sleep(1000); // Wait for cart page load
        boolean isCartEmpty = driver.findElements(By.className("cart_item")).isEmpty();
        Assert.assertTrue(isCartEmpty, "Cart should be empty when no products are added");
    }

    // Negative Scenario 2: Attempt to remove non-existent item from cart
    @Test
    public void testRemoveNonExistentItem() throws InterruptedException {
        // Add product to cart and navigate to cart page
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

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