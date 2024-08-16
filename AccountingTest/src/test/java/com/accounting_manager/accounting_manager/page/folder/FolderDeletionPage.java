package com.accounting_manager.accounting_manager.page.folder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FolderDeletionPage extends FolderBasePage {

    public FolderDeletionPage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(className = "tools-td")
    private static WebElement toolMenu;

    @FindBy(id = "submit")
    private static WebElement submitButton;

    /**
     * This method is used to simulate the action of clicking the 'add to favorites' button on the webpage.
     */
    public void clicksTheDeleteButton() {
        Actions action = new Actions(driver);
        WebElement option = toolMenu.findElement(By.name("deleteFolder"));
        action.moveToElement(toolMenu);
        action.moveToElement(option).click().perform();
    }

    /**
     * This method is used to simulate the action of clicking the 'submit' button on the webpage.
     */
    public void clicksTheConfirmDeletionButtonInTheModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
    }

}