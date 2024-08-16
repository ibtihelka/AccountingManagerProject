package com.accounting_manager.accounting_manager.tests.document;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.page.document.DocumentBasePage;
import com.accounting_manager.accounting_manager.page.document.DocumentCreationPage;
import com.accounting_manager.accounting_manager.page.folder.FolderBasePage;
import com.accounting_manager.accounting_manager.page.folder.FolderCreationPage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DocumentCreationTest extends Base {

    private LoginPage loginPage;
    private DocumentCreationPage documentCreationPage;
    private FolderBasePage folderBasePage;
    private FolderCreationPage folderCreationPage;
    private String currentTestFolderName = "";
    private String documentType;

    @BeforeClass
    @Parameters({"document-type"})
    public void setupClass(String documentType) {
        this.documentType= documentType;
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderCreationPage = new FolderCreationPage();
        currentTestFolderName = folderCreationPage.createNewFolder(documentType+"S");
        DocumentBasePage.setSelectedFolder(currentTestFolderName);
        driver.quit();
    }

    @BeforeMethod
    public void setup() {
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderBasePage = new FolderBasePage();
        folderBasePage.searchFolderByName(DocumentBasePage.getSelectedFolder());
        folderBasePage.doubleClicksTheFirstDisplayedFolder();
        documentCreationPage = new DocumentCreationPage(documentType);

    }

    @Test(description = "Successful Document Upload", priority = 1)
    public void testSuccessfulDocumentUpload() {
        documentCreationPage.uploadDocuments(3);
        documentCreationPage.clicksTheSubmitButton();
        documentCreationPage.successInvoiceUploadMessageDisplayedAssertion();
    }

    @Test(description = "Partial Successful Document Upload - 2 of the 3 documents already exist", priority = 2)
    public void testPartialSuccessfulDocumentUpload() {
        documentCreationPage.uploadDocuments(4);
        documentCreationPage.clicksTheSubmitButton();
        documentCreationPage.partialSuccessInvoiceUploadMessageDisplayedAssertion();
    }

    @Test(description = "Failed Document Upload - Documents Already Exist", priority = 3)
    public void testFailedDocumentUpload() {
        documentCreationPage.uploadDocuments(4);
        documentCreationPage.clicksTheSubmitButton();
        documentCreationPage.errorSnackbarOperationMessageDisplayAssertion();
    }

}
