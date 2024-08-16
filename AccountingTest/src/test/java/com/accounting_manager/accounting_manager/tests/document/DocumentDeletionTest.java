package com.accounting_manager.accounting_manager.tests.document;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.page.document.DocumentBasePage;
import com.accounting_manager.accounting_manager.page.document.DocumentDeletionPage;
import com.accounting_manager.accounting_manager.page.folder.FolderBasePage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DocumentDeletionTest extends Base {

    private DocumentDeletionPage documentDeletionPage;
    private LoginPage loginPage;
    private FolderBasePage folderBasePage;

    @BeforeMethod
    public void setup() {
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderBasePage = new FolderBasePage();
        folderBasePage.searchFolderByName(DocumentBasePage.getSelectedFolder());
        folderBasePage.doubleClicksTheFirstDisplayedFolder(true);
        documentDeletionPage = new DocumentDeletionPage();
    }

    @Test(description = "Successful Document Delete", priority = 1)
    public void testSuccessfulDocumentDelete() {
        documentDeletionPage.clicksTheToolMenuOfTheFirstDisplayedInvoice();
        documentDeletionPage.clicksTheDeleteButton();
        documentDeletionPage.clicksTheConfirmDeleteButton();
        documentDeletionPage.successSnackbarOperationMessageDisplayAssertion();
    }

    @Test(description = "Successful Multiple Documents Delete", priority = 2)
    public void testSuccessfulMultipleDocumentsDelete() {
        documentDeletionPage.selectMultipleDocuments();
        documentDeletionPage.clicksTheOperationsButton();
        documentDeletionPage.clicksTheDeleteMultipleDocumentsButton();
        documentDeletionPage.clicksTheConfirmMultipleDocumentsDeletionButton();
        documentDeletionPage.successSnackbarOperationMessageDisplayAssertion();
    }

    @Test(description = "Successful All Documents Delete", priority = 3)
    public void testSuccessfulAllDocumentsDelete() {
        documentDeletionPage.clicksSelectAllDocumentsCheckBox();
        documentDeletionPage.clicksTheOperationsButton();
        documentDeletionPage.clicksTheDeleteMultipleDocumentsButton();
        documentDeletionPage.clicksTheConfirmMultipleDocumentsDeletionButton();
        documentDeletionPage.successSnackbarOperationMessageDisplayAssertion();
    }

}
