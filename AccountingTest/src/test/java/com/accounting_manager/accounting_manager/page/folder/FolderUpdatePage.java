package com.accounting_manager.accounting_manager.page.folder;

import com.accounting_manager.accounting_manager.utils.Base;
import com.accounting_manager.accounting_manager.utils.TestDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FolderUpdatePage extends FolderBasePage {

    public FolderUpdatePage(){
        PageFactory.initElements(Base.driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(className = "tools-td")
    private static WebElement toolMenu;

    @FindBy(id = "submit")
    private static WebElement submitButton;
    @FindBy(id = "folderName")
    private static WebElement folderNameBox;
    @FindBy(id = "folderDescription")
    private static WebElement folderDescriptionBox;

    private String folderName = "INVOICESFolderQATEST"+ TestDataGenerator.generateRandomNumber(4) + "Updated";

    @FindBy(id = "search")
    private static WebElement searchBox;

    /**
     * This method is used to search a test folder
     */
    public void searchTestFolder(){
        searchBox.sendKeys("QATEST");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to simulate the action of clicking the tool menu button
     * of the first displayed folder on the webpage.
     *
     * @return
     */
    public String clicksTheToolMenuOfTheFirstDisplayedFolder() {
        toolMenu.click();
        return null;
    }

    /**
     * This method is used to simulate the action of clicking the 'modify' button on the webpage.
     */
    public void clicksTheModifyButton() {
        Actions action = new Actions(Base.driver);
        WebElement option = toolMenu.findElement(By.name("editFolder"));
        action.moveToElement(toolMenu);
        action.moveToElement(option).click().perform();
    }

    /**
     * This method is used to enter valid information into the respective fields to update the folder.
     */
    public void updatesFolderInformation() {
        folderNameBox.clear();
        folderDescriptionBox.clear();
        folderNameBox.sendKeys(folderName);
        folderDescriptionBox.sendKeys(folderName);
    }

    /**
     * This method is used to simulate the action of clicking the 'submit' button on the webpage.
     */
    public void clicksTheSubmitButton() {
        WebDriverWait wait = new WebDriverWait(Base.driver, Duration.ofSeconds(8));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
    }

}