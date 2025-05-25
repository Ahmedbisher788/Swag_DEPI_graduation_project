package FunctionalityTesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FooterTests {
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

    // Happy Scenario 1: Verify presence of all social media links
    @Test
    public void testSocialMediaLinksPresence() throws InterruptedException {
        // Verify social media links in footer
        Thread.sleep(1000); // Wait for page load
        boolean isTwitterPresent = driver.findElements(By.cssSelector("a[href='https://twitter.com/saucelabs']")).size() > 0;
        boolean isFacebookPresent = driver.findElements(By.cssSelector("a[href='https://www.facebook.com/saucelabs']")).size() > 0;
        boolean isLinkedInPresent = driver.findElements(By.cssSelector("a[href='https://www.linkedin.com/company/sauce-labs/']")).size() > 0;
        Assert.assertTrue(isTwitterPresent, "Twitter link missing in footer");
        Assert.assertTrue(isFacebookPresent, "Facebook link missing in footer");
        Assert.assertTrue(isLinkedInPresent, "LinkedIn link missing in footer");
    }

    // Happy Scenario 2: Click Twitter link and verify URL
    @Test
    public void testTwitterLinkNavigation() throws InterruptedException {
        // Click Twitter link
        driver.findElement(By.cssSelector("a[href='https://twitter.com/saucelabs']")).click();

        // Switch to new window (if opened) and verify URL
        Thread.sleep(1000); // Wait for new window
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("twitter.com/saucelabs") || currentUrl.contains("x.com/saucelabs"),
                "Did not navigate to Twitter page");
    }
}
