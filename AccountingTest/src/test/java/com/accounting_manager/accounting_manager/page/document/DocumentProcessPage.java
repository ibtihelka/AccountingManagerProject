package com.accounting_manager.accounting_manager.page.document;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DocumentProcessPage extends DocumentBasePage {

    public DocumentProcessPage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(id = "acceptAll")
    private static WebElement acceptAllButton;

    @FindBy(id = "rejectAll")
    private static WebElement rejectAllButton;

    /**
     * This method is used to simulate the action of clicking the 'accept all' button on the webpage.
     */
    public void clicksTheAcceptAllButton() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(90));
        wait.until(ExpectedConditions.elementToBeClickable(acceptAllButton));
        acceptAllButton.click();
    }

    /**
     * This method is used to simulate the action of clicking the 'reject all' button on the webpage.
     */
    public void clicksTheRejectAllButton() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(90));
        wait.until(ExpectedConditions.elementToBeClickable(rejectAllButton));
        rejectAllButton.click();
    }
}