package com.accounting_manager.accounting_manager.page.folder;

import com.accounting_manager.accounting_manager.utils.Base;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FolderBasePage extends Base {

    public FolderBasePage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(id = "search")
    private static WebElement searchBox;

    @FindBy(className = "tools-td")
    private static WebElement toolMenu;

    /**
     * This method is used to assert that the folder was created successfully (folder displayed by searching for it).
     */
    public void folderAppearInInterfaceAssertion(String folderName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@name='folderName' and contains(text(), '" + folderName + "')]")));
        List<WebElement> folderElements = driver.findElements(By.xpath("//td[@name='folderName' and contains(text(), '" + folderName + "')]"));
        Assert.assertFalse(folderElements.isEmpty(), "Folder named '" + folderName + "' should be displayed but it's not found.");
    }

    /**
     * This method is used to assert that the folder is disappeared from the current interface.
     */
    public void folderDisappearedFromInterfaceAssertion() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(18));
        ExpectedCondition<Boolean> customCondition = webDriver -> driver.findElements(By.className("hint-text")).size()==0;
        wait.until(customCondition);
        Assert.assertTrue(driver.findElements(By.className("hint-text")).size()==0);
    }

    /**
     * This method is used to search folder by name
     */
    public void searchFolderByName(String folderName){
        searchBox.sendKeys(folderName);
    }

    /**
     * This method is used to simulate the action of clicking the tool menu button
     * of the first displayed folder on the webpage.
     *
     * @return folderName
     */
    public String clicksTheToolMenuOfTheFirstDisplayedFolder() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(toolMenu));
        wait.until(ExpectedConditions.elementToBeClickable(toolMenu.findElement(By.xpath("./ancestor::tr"))));
        toolMenu.click();
        return driver.findElement(By.xpath("//td[@name='folderName']")).getText();
    }

    /**
     * This method is used to simulate the action of double-clicking the first empty/filled displayed folder on the webpage.
     * @param isFolderFilled boolean value to determine if the folder is filled or empty
     *
     * @return folderName
     */
    public String doubleClicksTheFirstDisplayedFolder(boolean isFolderFilled) {
        try {
            Thread.sleep(3000);
            Actions action = new Actions(driver);
            WebElement targetRow;
            if(isFolderFilled){
                targetRow = driver.findElement(By.xpath("//tbody//tr[td[@name='documentsCount' and text()!='0']]"));
            }
            else {
                targetRow = driver.findElement(By.xpath("//tbody//tr[td[@name='documentsCount' and text()='0']]"));
            }
            var folderName= targetRow.findElement(By.name("folderName")).getText();
            action.moveToElement(targetRow).doubleClick().build().perform();
            return folderName;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to simulate the action of double-clicking the first displayed folder on the webpage.
     */
    public void doubleClicksTheFirstDisplayedFolder() {
        try {
            Thread.sleep(7000);
            //folderName= driver.findElement(By.tagName("tbody")).findElement(By.name("folderName")).getText();
            Actions action = new Actions(driver);
            action.moveToElement(driver.findElement(By.tagName("tbody")).findElement(By.tagName("tr"))).doubleClick().build().perform();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * This method is used to assert that the success snackbar message is displayed.
     */
    public void successSnackbarOperationMessageDisplayAssertion() {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(8));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[contains(@class,'operation-success')]"))));
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(@class,'operation-success')]")).isDisplayed());
    }
}