package com.accounting_manager.accounting_manager.tests.document;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.page.document.DocumentBasePage;
import com.accounting_manager.accounting_manager.page.document.DocumentUpdatePage;
import com.accounting_manager.accounting_manager.page.document.bank_statement.BankStatementUpdatePage;
import com.accounting_manager.accounting_manager.page.document.invoice.InvoiceUpdatePage;
import com.accounting_manager.accounting_manager.page.folder.FolderBasePage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DocumentUpdateTest extends Base {

    private DocumentUpdatePage documentUpdatePage;
    private LoginPage loginPage;
    private FolderBasePage folderBasePage;

    @BeforeMethod
    @Parameters({"document-type"})
    public void setup(String documentType) {
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderBasePage = new FolderBasePage();
        folderBasePage.searchFolderByName(DocumentBasePage.getSelectedFolder());
        folderBasePage.doubleClicksTheFirstDisplayedFolder(true);
        switch (documentType){
            case "INVOICE":
                documentUpdatePage = new InvoiceUpdatePage();
                break;
            case "BANK_STATEMENT":
                documentUpdatePage = new BankStatementUpdatePage();
                break;
            default:
                throw new RuntimeException("Invalid document type");
        }

    }

    @Test(description = "Successful Invoice Update")
    public void testSuccessfulInvoiceUpdate() {
        documentUpdatePage.clicksTheToolMenuOfTheFirstDisplayedInvoice();
        documentUpdatePage.clicksTheUpdateButton();
        documentUpdatePage.updateDocumentInformation();
        documentUpdatePage.clicksTheSubmitButton();
        documentUpdatePage.successSnackbarOperationMessageDisplayAssertion();
    }

}
