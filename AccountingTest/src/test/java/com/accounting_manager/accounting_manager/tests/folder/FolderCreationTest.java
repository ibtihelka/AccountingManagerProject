package com.accounting_manager.accounting_manager.tests.folder;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.page.folder.FolderCreationPage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FolderCreationTest extends Base {

    private FolderCreationPage folderCreationPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderCreationPage = new FolderCreationPage();
        folderCreationPage.loadUrl("manageFoldersRoute");
    }

    @Test(description = "Successful Folder Creation For The Logged-In User")
    public void testSuccessfulFolderCreationForLoggedInUser() {
        folderCreationPage.createNewFolder("BANK_STATEMENTS");
    }

    @Test(description = "Successful Folder Creation For New Physical Client")
    public void testSuccessfulFolderCreationForNewPhysicalClient() {
        folderCreationPage.clicksTheAddFolderButton();
        folderCreationPage.clicksTheNewPhysicalClientRadioButton();
        folderCreationPage.entersValidPhysicalClientInformation();
        folderCreationPage.clicksTheNextButton();
        String folderName= folderCreationPage.entersValidFolderInformation("INVOICES");
        folderCreationPage.clicksTheSubmitButton();
        folderCreationPage.successSnackbarOperationMessageDisplayAssertion();
        folderCreationPage.searchFolderByName(folderName);
        folderCreationPage.folderAppearInInterfaceAssertion(folderName);
    }

    @Test(description = "Successful Folder Creation For New Legal Client")
    public void testSuccessfulFolderCreationForNewLegalClient() {
        folderCreationPage.clicksTheAddFolderButton();
        folderCreationPage.clicksTheNewLegalClientRadioButton();
        folderCreationPage.entersValidLegalClientInformation();
        folderCreationPage.clicksTheNextButton();
        String folderName= folderCreationPage.entersValidFolderInformation("INVOICES");
        folderCreationPage.clicksTheSubmitButton();
        folderCreationPage.successSnackbarOperationMessageDisplayAssertion();
        folderCreationPage.searchFolderByName(folderName);
        folderCreationPage.folderAppearInInterfaceAssertion(folderName);
    }
}
