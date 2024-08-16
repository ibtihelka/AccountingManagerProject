package com.accounting_manager.accounting_manager.page.auth;

import com.accounting_manager.accounting_manager.utils.Base;
import com.accounting_manager.accounting_manager.utils.ConfigUtils;
import com.accounting_manager.accounting_manager.utils.TestDataGenerator;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage extends Base {

    public RegisterPage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    //Physical form inputs
    @FindBy(name = "physicalPhoneNumber")
    private static WebElement physicalPhoneNumberBox;
    @FindBy(name = "physicalEmail")
    private static WebElement physicalEmailBox;
    @FindBy(name = "physicalPassword")
    private static WebElement physicalPasswordBox;
    @FindBy(name = "physicalConfirmPassword")
    private static WebElement physicalConfirmPasswordBox;
    @FindBy(name = "nic")
    private static WebElement nicBox;
    @FindBy(name = "firstname")
    private static WebElement firstnameBox;
    @FindBy(name = "lastname")
    private static WebElement lastnameBox;
    private static Select genderDropdown;
    @FindBy(name = "submitPhysicalForm")
    private static WebElement registerPhysicalButton;
    //Legal form inputs
    @FindBy(name = "legalPhoneNumber")
    private static WebElement legalPhoneNumberBox;
    @FindBy(name = "legalEmail")
    private static WebElement legalEmailBox;
    @FindBy(name = "legalPassword")
    private static WebElement legalPasswordBox;
    @FindBy(name = "legalConfirmPassword")
    private static WebElement legalConfirmPasswordBox;
    @FindBy(name = "siretNumber")
    private static WebElement siretNumberBox;
    @FindBy(name = "legalName")
    private static WebElement legalNameBox;
    @FindBy(name = "submitLegalForm")
    private static WebElement registerLegalButton;

    @FindBy(id = "legalTab")
    private static WebElement legalTab;

    private String generatedEmail = TestDataGenerator.generateUniqueEmail();
    private String generatedRandomStringNumber = TestDataGenerator.generateRandomNumber(8);

    /**
     * This method is used to enter valid information in the physical form into the respective fields.
     */
    public void entersValidPhysicalUserInformation() {
        physicalEmailBox.sendKeys(generatedEmail);
        physicalPhoneNumberBox.sendKeys(generatedRandomStringNumber);
        physicalPasswordBox.sendKeys("00000000");
        physicalConfirmPasswordBox.sendKeys("00000000");
        nicBox.sendKeys(generatedRandomStringNumber);
        firstnameBox.sendKeys("John");
        lastnameBox.sendKeys("Smith");
        genderDropdown = new Select(driver.findElement(By.name("gender")));
        genderDropdown.selectByValue("male");
    }

    public void clicksTheLegalFormTab() {
        legalTab.click();
    }

    /**
     * This method is used to enter valid information in the legal form into the respective fields.
     */
    public void entersValidLegalUserInformation() {
        legalEmailBox.sendKeys(generatedEmail);
        legalPhoneNumberBox.sendKeys(generatedRandomStringNumber);
        legalPasswordBox.sendKeys("00000000");
        legalConfirmPasswordBox.sendKeys("00000000");
        siretNumberBox.sendKeys(TestDataGenerator.generateRandomNumber(14));
        legalNameBox.sendKeys("COMPANY "+TestDataGenerator.generateAlphaString(4));
    }

    /**
     * This method is used to simulate the action of clicking the register button on the webpage.
     */
    public void clicksTheRegisterPhysicalButton() {
        registerPhysicalButton.click();
    }

    /**
     * This method is used to simulate the action of clicking the register button on the webpage.
     */
    public void clicksTheRegisterLegalButton() {
        registerLegalButton.click();
    }

    /**
     * This method is used to assert that the register was successful.
     * It checks if the registered user is redirected to the login page.
     */
    public void successfulRegisterAssertion() {
        new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.invisibilityOf(registerLegalButton));
        new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.invisibilityOf(registerPhysicalButton));
        new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.urlContains("login"));
        String newUrl = ConfigUtils.getInstance().getUrl("loginRoute");
        Assert.assertEquals(newUrl, driver.getCurrentUrl());
    }

    /**
     * This method is used to enter valid information into the respective fields except confirm password field.
     */
    public void entersDifferentConfirmPassword() {
        nicBox.sendKeys(generatedRandomStringNumber);
        firstnameBox.sendKeys("John");
        lastnameBox.sendKeys("Smith");
        physicalPhoneNumberBox.sendKeys(generatedRandomStringNumber);
        physicalEmailBox.sendKeys(generatedEmail);
        genderDropdown = new Select(driver.findElement(By.name("gender")));
        genderDropdown.selectByValue("male");
        physicalPasswordBox.sendKeys("00000000");
        physicalConfirmPasswordBox.sendKeys(generatedRandomStringNumber);
    }

    /**
     * Assert that the register button is disabled.
     */
    public void disabledRegisterButtonAssertion() {
        Assert.assertFalse(registerLegalButton.isEnabled());
        Assert.assertFalse(registerPhysicalButton.isEnabled());
    }

}
