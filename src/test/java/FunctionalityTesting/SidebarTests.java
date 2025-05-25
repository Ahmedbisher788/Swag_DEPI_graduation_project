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

public class SidebarTests {
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

    // Happy Scenario 1: Verify all expected menu items in sidebar
    @Test
    public void testSidebarMenuItems() throws InterruptedException {
        // Open sidebar
        driver.findElement(By.id("react-burger-menu-btn")).click();

        // Verify menu items
        Thread.sleep(1000); // Wait for sidebar to open
        boolean isAllItemsPresent = driver.findElements(By.id("inventory_sidebar_link")).size() > 0;
        boolean isAboutPresent = driver.findElements(By.id("about_sidebar_link")).size() > 0;
        boolean isLogoutPresent = driver.findElements(By.id("logout_sidebar_link")).size() > 0;
        boolean isResetPresent = driver.findElements(By.id("reset_sidebar_link")).size() > 0;
        Assert.assertTrue(isAllItemsPresent, "All Items link missing in sidebar");
        Assert.assertTrue(isAboutPresent, "About link missing in sidebar");
        Assert.assertTrue(isLogoutPresent, "Logout link missing in sidebar");
        Assert.assertTrue(isResetPresent, "Reset App State link missing in sidebar");
    }

    // Happy Scenario 2: Click Logout link and verify navigation to login page
    @Test
    public void testLogoutNavigation() throws InterruptedException {
        // Open sidebar and click Logout
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(500); // Wait for sidebar to open
        driver.findElement(By.id("logout_sidebar_link")).click();

        // Verify navigation to login page
        Thread.sleep(1000); // Wait for page load
        boolean isLoginButtonPresent = driver.findElements(By.id("login-button")).size() > 0;
        Assert.assertTrue(isLoginButtonPresent, "Did not navigate to login page after logout");
    }

    // Happy Scenario 3: Click About link and verify navigation to Sauce Labs website
    @Test
    public void testAboutLinkNavigation() throws InterruptedException {
        // Open sidebar and click About
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(500); // Wait for sidebar to open
        driver.findElement(By.id("about_sidebar_link")).click();

        // Verify navigation to Sauce Labs website
        Thread.sleep(1000); // Wait for page load
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("saucelabs.com"),
                "Did not navigate to Sauce Labs website");
    }

    // Negative Scenario 1: Verify non-existent menu item (Profile)
    @Test
    public void testNonExistentMenuItem() throws InterruptedException {
        // Open sidebar
        driver.findElement(By.id("react-burger-menu-btn")).click();

        // Verify Profile link is not present
        Thread.sleep(1000); // Wait for sidebar to open
        boolean isProfilePresent = driver.findElements(By.id("profile_sidebar_link")).size() > 0;
        Assert.assertFalse(isProfilePresent, "Profile link should not be present in sidebar");
    }

    // Negative Scenario 2: Attempt to access menu items without opening sidebar
    @Test
    public void testAccessMenuWithoutOpeningSidebar() throws InterruptedException {
        // Check if logout link is visible without opening sidebar
        boolean isLogoutVisible = false;
        if (driver.findElements(By.id("logout_sidebar_link")).size() > 0) {
            WebElement logoutLink = driver.findElement(By.id("logout_sidebar_link"));
            isLogoutVisible = logoutLink.isDisplayed();
        }

        // Verify menu items are not visible
        Thread.sleep(1000); // Wait for any potential rendering
        Assert.assertFalse(isLogoutVisible, "Logout link should not be visible without opening sidebar");
    }
}