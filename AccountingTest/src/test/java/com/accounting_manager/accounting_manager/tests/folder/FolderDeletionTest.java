package com.accounting_manager.accounting_manager.tests.folder;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.page.folder.FolderDeletionPage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FolderDeletionTest extends Base {

    private FolderDeletionPage folderDeletionPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderDeletionPage = new FolderDeletionPage();
        folderDeletionPage.loadUrl("manageFoldersRoute");
    }

    @Test(description = "Successful Delete Folder")
    public void testSuccessfulFolderDelete() {
        folderDeletionPage.searchFolderByName("INVOICESFolderQATEST");
        String selectedFolderName = folderDeletionPage.clicksTheToolMenuOfTheFirstDisplayedFolder();
        folderDeletionPage.clicksTheDeleteButton();
        folderDeletionPage.clicksTheConfirmDeletionButtonInTheModal();
        folderDeletionPage.successSnackbarOperationMessageDisplayAssertion();
        folderDeletionPage.searchFolderByName(selectedFolderName);
        folderDeletionPage.folderDisappearedFromInterfaceAssertion();
    }
}
