package com.accounting_manager.accounting_manager.tests.folder;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.page.folder.FolderUpdatePage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FolderUpdateTest extends Base {

    private FolderUpdatePage folderUpdatePage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderUpdatePage = new FolderUpdatePage();
        folderUpdatePage.loadUrl("manageFoldersRoute");
    }

    @Test(description = "Successful Folder Update")
    public void testSuccessfulFolderUpdate() {
        folderUpdatePage.searchTestFolder();
        folderUpdatePage.clicksTheToolMenuOfTheFirstDisplayedFolder();
        folderUpdatePage.clicksTheModifyButton();
        folderUpdatePage.updatesFolderInformation();
        folderUpdatePage.clicksTheSubmitButton();
        folderUpdatePage.successSnackbarOperationMessageDisplayAssertion();
    }
}
