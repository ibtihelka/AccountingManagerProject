package com.accounting_manager.accounting_manager.page.document;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DocumentDeletionPage extends DocumentBasePage {

    public DocumentDeletionPage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(id = "submit")
    private static WebElement submitButton;

    @FindBy(className = "tools-td")
    private static WebElement toolMenu;


    @FindBy(id = "confirmMultipleDocumentsDeletion")
    private static WebElement confirmMultipleDocumentsDeletionButton;

    /**
     * This method is used to simulate the action of clicking the 'delete' button on the webpage.
     */
    public void clicksTheDeleteButton() {
        Actions action = new Actions(driver);
        WebElement option = toolMenu.findElement(By.name("deleteDocument"));
        action.moveToElement(toolMenu);
        action.moveToElement(option).click().perform();
    }

    /**
     * This method is used to simulate the action of clicking the 'submit' button on the 'confirm delete' modal.
     */
    public void clicksTheConfirmDeleteButton() {
        submitButton.click();
    }

    public void clicksTheConfirmMultipleDocumentsDeletionButton() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(confirmMultipleDocumentsDeletionButton));
        confirmMultipleDocumentsDeletionButton.click();
    }

}