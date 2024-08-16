package com.accounting_manager.accounting_manager.page.auth;

import com.accounting_manager.accounting_manager.utils.Base;
import com.accounting_manager.accounting_manager.utils.ConfigUtils;
import com.accounting_manager.accounting_manager.utils.TestDataGenerator;
import org.openqa.selenium.By;
import org.testng.Assert;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends Base {

    public LoginPage() {
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(name = "email")
    private static WebElement emailBox;
    @FindBy(name = "password")
    private static WebElement passwordBox;
    @FindBy(xpath = "//button[@type='submit']")
    private static WebElement loginButton;

    private static String generatedEmail = TestDataGenerator.generateUniqueEmail();
    /**
     * This method is used to enter a valid username and password into the respective fields.
     */
    public void entersValidUsernameAndPassword() {
        emailBox.sendKeys("user@gmail.com");
        passwordBox.sendKeys("00000000");
    }

    /**
     * This method is used to simulate the action of clicking the login button on the webpage.
     */
    public void clicksTheLoginButton() {
        loginButton.click();
    }

    /**
     * This method is used to assert that the login was successful.
     * It checks of the logged-in user is redirected to the dashboard.
     */
    public void successfulLoginAssertion() {
        driver.manage().deleteAllCookies();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("manage"));
        String newUrl = ConfigUtils.getInstance().getUrl("manageFoldersRoute");
        Assert.assertEquals(newUrl,driver.getCurrentUrl());
        Assert.assertNotNull(driver.manage().getCookieNamed("refreshToken"));
        Assert.assertNotNull(driver.manage().getCookieNamed("jwtToken"));
    }

    /**
     * This method is used to enter an invalid username and password into the respective fields.
     */
    public void entersInvalidUsernameAndPassword() {
        emailBox.sendKeys(generatedEmail);
        passwordBox.sendKeys("Test.102030");
    }

    /**
     * This method is used to enter inactive account credentials into the respective fields.
     */
    public void entersNotActivatedAccountCredentials() {
        emailBox.sendKeys(generatedEmail);
        passwordBox.sendKeys("Test.102030");
    }

    public void performLogin() {
        entersValidUsernameAndPassword();
        clicksTheLoginButton();
        successfulLoginAssertion();
    }

    /**
     * This method is used to assert that the error snackbar message is displayed.
     */
    public void errorSnackbarOperationMessageDisplayAssertion() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[contains(@class,'operation-error')]"))));
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(@class,'operation-error')]")).isDisplayed());
    }

}
