package com.accounting_manager.accounting_manager.utils;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

/**
 * This class provides the base functionality for launching a web browser for testing.
 * It supports Chrome, Firefox, and Edge browsers.
 */
@Log4j2
public class Base {

    /**
     * WebDriver instance used to interact with the web browser.
     */
    public static WebDriver driver;

    /**
     * This method is used to launch a web browser.
     * The browser type can be specified using the "browser" system property.
     * @return WebDriver instance for the launched browser.
     * @throws RuntimeException if the specified browser is not supported.
     */
    public WebDriver launchBrowser() {

        String browser = System.getProperty("browser", "CHROME");
        switch (browser) {
            case "CHROME" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--start-maximized");
                options.addArguments("--headless");
                driver = new ChromeDriver(options);
            }
            case "FIREFOX" -> {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--start-maximized");
                options.addArguments("--headless");
                driver = new FirefoxDriver(options);
            }
            case "EDGE" -> {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--start-maximized");
                options.addArguments("--headless");
                driver = new EdgeDriver(options);
            }
            default -> {
                throw new RuntimeException("Browser is not supported");
            }
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        return driver;
    }

    /**
     * This method is used to close the browser and take a screenshot.
     * The screenshot is attached to the test result.
     * @param result The result of the test.
     * @throws IOException if an error occurs while taking the screenshot.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (driver != null) {
                // Take a screenshot of the entire page
                BufferedImage image = new AShot()
                        .shootingStrategy(ShootingStrategies.simple())
                        .takeScreenshot(driver)
                        .getImage();

                // Convert the screenshot to bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] screenshot = baos.toByteArray();

                // Attach the screenshot to the test result
                result.setAttribute("screenshot", screenshot);
            }
        } catch (Exception e) {
            log.error("Error while taking screenshot: ", e);
        } finally {
            // Quit the driver to release system resources
            driver.quit();
        }
    }

    @AfterSuite
    public void tearDown(){
        driver.quit();
    }

    /**
     * This method is used to load a URL.
     * @param route The route to load.
     */
    public void loadUrl(String route) {
        String url = ConfigUtils.getInstance().getUrl(route);
        driver.get(url);
    }

    /**
     * This method is used to load a URL with a subroute.
     * @param route The route to load.
     * @param subRoute The sub route to load.
     */
    public void loadUrl(String route,String subRoute) {
        String url = ConfigUtils.getInstance().getUrl(route) + ConfigUtils.getInstance().getSubUrl(subRoute);
        driver.get(url);
    }

}