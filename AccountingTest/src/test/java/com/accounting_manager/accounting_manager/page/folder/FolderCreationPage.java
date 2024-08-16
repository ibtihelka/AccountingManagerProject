package com.accounting_manager.accounting_manager.page.folder;

import com.accounting_manager.accounting_manager.utils.TestDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FolderCreationPage extends FolderBasePage {

    public FolderCreationPage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(id = "addFolder")
    private static WebElement addFolderButton;
    @FindBy(id = "next")
    private static WebElement nextButton;
    @FindBy(id = "submit")
    private static WebElement submitButton;
    @FindBy(id = "folderName")
    private static WebElement folderNameBox;
    @FindBy(id = "folderDescription")
    private static WebElement folderDescriptionBox;

    @FindBy(name = "existingClientsRadioGroup")
    private static List<WebElement> existingClientsRadioGroup;
    @FindBy(xpath = "//input[contains(@name,'clientTypeRadioGroup') and contains(@value,'newPhysical')]")
    private static WebElement newPhysicalClientRadioButton;
    @FindBy(xpath = "//input[@name='clientTypeRadioGroup' and @value='newLegal']")
    private static WebElement newLegalClientRadioButton;

    //Physical form inputs
    @FindBy(name = "physicalPhoneNumber")
    private static WebElement physicalPhoneNumberBox;
    @FindBy(name = "physicalEmail")
    private static WebElement physicalEmailBox;
    @FindBy(name = "nic")
    private static WebElement nicBox;
    @FindBy(name = "firstname")
    private static WebElement firstnameBox;
    @FindBy(name = "lastname")
    private static WebElement lastnameBox;
    private static Select genderDropdown;
    //Legal form inputs
    @FindBy(name = "legalPhoneNumber")
    private static WebElement legalPhoneNumberBox;
    @FindBy(name = "legalEmail")
    private static WebElement legalEmailBox;
    @FindBy(name = "siretNumber")
    private static WebElement siretNumberBox;
    @FindBy(name = "legalName")
    private static WebElement legalNameBox;

    private String generatedEmail = TestDataGenerator.generateUniqueEmail();
    private String generatedRandomStringNumber = TestDataGenerator.generateRandomNumber(8);

    private String folderName = "QATEST"+TestDataGenerator.generateRandomNumber(4);

    /**
     * This method is used to simulate the action of clicking the add folder button on the webpage.
     */
    public void clicksTheAddFolderButton() {
        addFolderButton.click();
    }

    /**
     * This method is used to simulate the action of clicking the logged-in user radio button on the webpage.
     */
    public void selectLoggedInUserAsTheClientOfTheFolder() {
        existingClientsRadioGroup.get(0).click();
    }

    /**
     * This method is used to simulate the action of clicking the next button on the webpage.
     */
    public void clicksTheNextButton() {
        nextButton.click();
    }

    /**
     * This method is used to enter valid information into the respective fields.
     */
    public String entersValidFolderInformation(String type) {
        folderName = switch (type) {
            case "INVOICES", "BANK_STATEMENTS" -> type + "FOLDER" + folderName;
            default -> "FOLDER" + folderName;
        };
        folderNameBox.sendKeys(folderName);
        folderDescriptionBox.sendKeys(folderName);
        genderDropdown = new Select(driver.findElement(By.id("folderType")));
        if (!type.equals(""))
            genderDropdown.selectByValue(type);
        return folderName;
    }

    /**
     * This method is used to simulate the action of clicking the 'submit' button on the webpage.
     */
    public void clicksTheSubmitButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
    }

    /**
     * This method is used to simulate the action of clicking the new physical client radio button on the webpage.
     */
    public void clicksTheNewPhysicalClientRadioButton() {
        newPhysicalClientRadioButton.click();
    }

    /**
     * This method is used to enter valid information in the physical form into the respective fields.
     */
    public void entersValidPhysicalClientInformation() {
        physicalEmailBox.sendKeys(generatedEmail);
        physicalPhoneNumberBox.sendKeys(generatedRandomStringNumber);
        nicBox.sendKeys(generatedRandomStringNumber);
        firstnameBox.sendKeys("John");
        lastnameBox.sendKeys("Smith");
        genderDropdown = new Select(driver.findElement(By.name("gender")));
        genderDropdown.selectByValue("male");
    }

    /**
     * This method is used to simulate the action of clicking the new legal client radio button on the webpage.
     */
    public void clicksTheNewLegalClientRadioButton() {
        newLegalClientRadioButton.click();
    }

    /**
     * This method is used to enter valid information in the legal form into the respective fields.
     */
    public void entersValidLegalClientInformation() {
        legalEmailBox.sendKeys(generatedEmail);
        legalPhoneNumberBox.sendKeys(generatedRandomStringNumber);
        siretNumberBox.sendKeys(TestDataGenerator.generateRandomNumber(14));
        legalNameBox.sendKeys("COMPANY "+TestDataGenerator.generateAlphaString(4));
    }

    public String createNewFolder(String type) {
        clicksTheAddFolderButton();
        selectLoggedInUserAsTheClientOfTheFolder();
        clicksTheNextButton();
        entersValidFolderInformation(type);
        clicksTheSubmitButton();
        successSnackbarOperationMessageDisplayAssertion();
        searchFolderByName(folderName);
        folderAppearInInterfaceAssertion(folderName);
        return folderName;
    }

    /**
     * This method is used to assert that the success snackbar message is displayed.
     */
    @Override
    public void successSnackbarOperationMessageDisplayAssertion() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("submit"))));
        super.successSnackbarOperationMessageDisplayAssertion();
    }
}