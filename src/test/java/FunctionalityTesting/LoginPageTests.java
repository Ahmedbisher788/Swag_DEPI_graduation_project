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

public class LoginPageTests {
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

    // Happy Scenario 1: Login with standard_user and verify products page
    @Test
    public void testLoginWithStandardUser() throws InterruptedException {
        login("standard_user", "secret_sauce");
        // Verify successful navigation to products page
        Thread.sleep(1000); // Wait for page load
        WebElement productsTitle = driver.findElement(By.className("title"));
        Assert.assertEquals(productsTitle.getText(), "Products",
                "Did not navigate to products page after login");
    }

    // Happy Scenario 2: Login with problem_user and verify successful login
    @Test
    public void testLoginWithProblemUser() throws InterruptedException {
        login("problem_user", "secret_sauce");

        // Verify successful navigation to products page
        Thread.sleep(1000); // Wait for page load
        boolean isProductsPage = driver.findElements(By.className("inventory_list")).size() > 0;
        Assert.assertTrue(isProductsPage, "Did not navigate to products page after login");
    }

    // Happy Scenario 3: Login with visual_user and verify page title
    @Test
    public void testLoginWithVisualUser() throws InterruptedException {
        login("visual_user", "secret_sauce");

        // Verify page title after login
        Thread.sleep(1000); // Wait for page load
        String pageTitle = driver.getTitle();
        Assert.assertEquals(pageTitle, "Swag Labs",
                "Incorrect page title after login");
    }

    // Negative Scenario 1: Login with invalid credentials
    @Test
    public void testLoginWithInvalidCredentials() throws InterruptedException {
        login("invalid_user", "wrong_password");

        // Verify error message
        Thread.sleep(1000); // Wait for error to appear
        WebElement error = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(error.getText().contains("Username and password do not match"),
                "Expected invalid credentials error not displayed");
    }

    // Negative Scenario 2: Login with empty fields
    @Test
    public void testLoginWithEmptyFields() throws InterruptedException {
        login("", "");

        // Verify error message
        Thread.sleep(1000); // Wait for error to appear
        WebElement error = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(error.getText().contains("Username is required"),
                "Expected empty fields error not displayed");
    }
}