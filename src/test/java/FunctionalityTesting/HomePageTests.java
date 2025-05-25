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

public class HomePageTests {
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
    void tearDown() {
        if (driver != null) {
            driver.quit(); // Properly closes the browser
        }
    }

    // Happy Scenario 1: Verify product list is displayed and contains expected number of products
    @Test
    public void testProductListDisplay() throws InterruptedException {
        // Verify product list is displayed
        Thread.sleep(1000); // Wait for page load
        WebElement inventoryList = driver.findElement(By.className("inventory_list"));
        int productCount = inventoryList.findElements(By.className("inventory_item")).size();
        Assert.assertEquals(productCount, 6, "Incorrect number of products displayed");
    }

    // Happy Scenario 2: Add product to cart and verify cart badge
    @Test
    public void testAddProductToCart() throws InterruptedException {
        // Add product to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // Verify cart badge updates
        Thread.sleep(1000); // Wait for cart update
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "1", "Cart badge did not update correctly");
    }

    // Happy Scenario 3: Sort products by name (Z to A) and verify first product
    @Test
    public void testSortProductsByName() throws InterruptedException {
        // Sort products by Name (z to A)
        driver.findElement(By.className("product_sort_container")).click();
        driver.findElement(By.cssSelector("option[value='za']")).click();

        // Verify first product is correct
        Thread.sleep(1000); // Wait for sort to apply
        WebElement firstProduct = driver.findElement(By.className("inventory_item_name"));
        Assert.assertEquals(firstProduct.getText(), "Test.allTheThings() T-Shirt (Red)",
                "First product after sorting is incorrect");
    }

    // Negative Scenario 1: Attempt to add non-existent product to cart
    @Test
    public void testAddNonExistentProduct() throws InterruptedException {
        // Attempt to add a non-existent product (invalid ID)
        boolean isButtonPresent = driver.findElements(By.id("add-to-cart-non-existent-product")).size() > 0;

        // Verify cart badge is not updated (no product added)
        Thread.sleep(1000); // Wait for any potential update
        boolean isCartBadgePresent = driver.findElements(By.className("shopping_cart_badge")).size() > 0;
        Assert.assertFalse(isCartBadgePresent, "Cart badge should not appear for non-existent product");
        Assert.assertFalse(isButtonPresent, "Non-existent product button should not be present");
    }

    // Negative Scenario 2: Attempt to access product with invalid ID
    @Test
    public void testAccessInvalidProduct() throws InterruptedException {
        // Attempt to access a product with invalid ID (no direct link, simulate invalid interaction)
        boolean isInvalidProductPresent = driver.findElements(By.id("item_invalid_id_title_link")).size() > 0;

        // Verify no product details are displayed
        Thread.sleep(1000); // Wait for any potential load
        Assert.assertFalse(isInvalidProductPresent, "Invalid product link should not be accessible");
    }
}