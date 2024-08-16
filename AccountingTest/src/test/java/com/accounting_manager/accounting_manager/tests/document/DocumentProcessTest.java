package com.accounting_manager.accounting_manager.tests.document;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.page.document.DocumentBasePage;
import com.accounting_manager.accounting_manager.page.document.DocumentProcessPage;
import com.accounting_manager.accounting_manager.page.folder.FolderBasePage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DocumentProcessTest extends Base {

    LoginPage loginPage;
    FolderBasePage folderBasePage;
    DocumentProcessPage documentProcessPage;

    @BeforeMethod
    public void setup(){
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderBasePage = new FolderBasePage();
        folderBasePage.searchFolderByName(DocumentBasePage.getSelectedFolder());
        folderBasePage.doubleClicksTheFirstDisplayedFolder(true);
        documentProcessPage = new DocumentProcessPage();
    }

    @Test(description = "Successful Document Process - Reject All Documents", priority = 1)
    public void testSuccessfulDocumentProcessRejectAll() {
        documentProcessPage.clicksSelectAllDocumentsCheckBox();
        documentProcessPage.clicksTheOperationsButton();
        documentProcessPage.clicksTheProcessDocumentsButton();
        documentProcessPage.clicksTheRejectAllButton();
        documentProcessPage.successSnackbarOperationMessageDisplayAssertion();
    }

    @Test(description = "Successful Document Process - Accept All Documents", priority = 2)
    public void testSuccessfulDocumentProcessAcceptAll() {
        documentProcessPage.clicksSelectAllDocumentsCheckBox();
        documentProcessPage.clicksTheOperationsButton();
        documentProcessPage.clicksTheProcessDocumentsButton();
        documentProcessPage.clicksTheAcceptAllButton();
        documentProcessPage.successSnackbarOperationMessageDisplayAssertion();
    }

}
