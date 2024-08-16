package com.accounting_manager.accounting_manager.page.document;

import com.accounting_manager.accounting_manager.utils.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class DocumentBasePage extends Base {

    public DocumentBasePage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(className = "hint-text")
    private static WebElement invoicesCountText;

    @FindBy(id = "search")
    private static WebElement searchBox;

    @FindBy(className = "tools-td")
    private static WebElement toolMenu;

    @FindBy(name = "documentCheckBox")
    private static List<WebElement> documentCheckBoxes;

    @FindBy(id = "operationsButton")
    private static WebElement operationsButton;

    @FindBy(id = "operationsMenu")
    private static WebElement operationsMenu;

    @FindBy(id = "selectAll")
    private static WebElement selectAllDocumentsCheckbox;

    private static String selectedFolder;

    public static String getSelectedFolder() {
        return selectedFolder;
    }

    public static void setSelectedFolder(String selectedFolder) {
        DocumentBasePage.selectedFolder = selectedFolder;
    }

    /**
     * This method is used to simulate the action of clicking the tool menu button
     * of the first displayed invoice on the webpage.
     */
    public void clicksTheToolMenuOfTheFirstDisplayedInvoice() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(toolMenu));
        wait.until(ExpectedConditions.elementToBeClickable(toolMenu.findElement(By.xpath("./ancestor::tr"))));
        toolMenu.click();
    }

    public void selectMultipleDocuments() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.name("documentCheckBox"),0));
        documentCheckBoxes.get(0).click();
        documentCheckBoxes.get(1).click();
    }

    public void clicksTheOperationsButton() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(operationsButton));
        operationsButton.click();
    }

    public void clicksTheDeleteMultipleDocumentsButton() {
        Actions action = new Actions(driver);
        WebElement option = operationsMenu.findElement(By.id("deleteMultipleDocuments"));
        action.moveToElement(operationsMenu);
        action.moveToElement(option).click().perform();
    }

    public void clicksTheProcessDocumentsButton() {
        Actions action = new Actions(driver);
        WebElement option = operationsMenu.findElement(By.id("processDocuments"));
        action.moveToElement(operationsMenu);
        action.moveToElement(option).click().perform();
    }

    public void clicksSelectAllDocumentsCheckBox() {
        selectAllDocumentsCheckbox.click();
    }


    /**
     * This method is used to assert that the success snackbar message is displayed.
     */
    public void successSnackbarOperationMessageDisplayAssertion() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[contains(@class,'operation-success')]"))));
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(@class,'operation-success')]")).isDisplayed());
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