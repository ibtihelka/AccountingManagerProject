package com.accounting_manager.accounting_manager.page.folder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FolderOperationsPage extends FolderBasePage {

    public FolderOperationsPage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Page Elements
     */
    @FindBy(className = "tools-td")
    private static WebElement toolMenu;


    /**
     * This method is used to simulate the action of clicking the 'add to favorites' button on the webpage.
     */
    public void clicksTheAddToFavoriteButton(String selectedFolderName) {
        Actions action = new Actions(driver);
        if (toolMenu.findElements(By.name("addFavorite")).size()==0) {
            clicksTheRemoveFromFavoriteButton();
            searchFolderByName(selectedFolderName);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            clicksTheToolMenuOfTheFirstDisplayedFolder();
        }
        WebElement option = toolMenu.findElement(By.name("addFavorite"));
        action.moveToElement(toolMenu);
        action.moveToElement(option).click().perform();
    }

    /**
     * This method is used to simulate the action of clicking the 'remove from favorites' button on the webpage.
     */
    public void clicksTheRemoveFromFavoriteButton() {
        Actions action = new Actions(driver);
        WebElement option = toolMenu.findElement(By.name("removeFavorite"));
        action.moveToElement(toolMenu);
        action.moveToElement(option).click().perform();
    }

    /**
     * This method is used to simulate the action of clicking the 'archive' button on the webpage.
     */
    public void clicksTheArchiveButton() {
        Actions action = new Actions(driver);
        WebElement option = toolMenu.findElement(By.name("archive"));
        action.moveToElement(toolMenu);
        action.moveToElement(option).click().perform();
    }

    /**
     * This method is used to simulate the action of clicking the 'unarchive' button on the webpage.
     */
    public void clicksTheUnarchiveButton() {
        Actions action = new Actions(driver);
        WebElement option = toolMenu.findElement(By.name("unarchive"));
        action.moveToElement(toolMenu);
        action.moveToElement(option).click().perform();
    }

}