package com.accounting_manager.accounting_manager.page.document;

import com.accounting_manager.accounting_manager.utils.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class DocumentUpdatePage extends DocumentBasePage {

    public DocumentUpdatePage(){
        PageFactory.initElements(Base.driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(id = "submit")
    private static WebElement submitButton;

    @FindBy(className = "tools-td")
    private static WebElement toolMenu;

    /**
     * This method is used to simulate the action of clicking the 'delete' button on the webpage.
     */
    public void clicksTheUpdateButton() {
        Actions action = new Actions(Base.driver);
        WebElement option = toolMenu.findElement(By.name("updateDocument"));
        action.moveToElement(toolMenu);
        action.moveToElement(option).click().perform();
    }

    /**
     * This method is used to simulate the action of clicking the 'submit' button on the 'confirm delete' modal.
     */
    public void clicksTheSubmitButton() {
        WebDriverWait wait = new WebDriverWait(Base.driver, Duration.ofSeconds(18));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
    }

    /**
     * This method is used to simulate the action of updating the document information on the webpage.
     * It should be implemented in the child classes.
     */
    public abstract void updateDocumentInformation();

}