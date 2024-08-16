package com.accounting_manager.accounting_manager.page.document;

import com.accounting_manager.accounting_manager.utils.Base;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.nio.file.Paths;
import java.time.Duration;

@Log4j2
public class DocumentCreationPage extends DocumentBasePage {

    /**
     * Page Elements
     */
    @FindBy(id = "uploadInvoices")
    private static WebElement fileInput;
    @FindBy(id = "submit")
    private static WebElement submitButton;
    @FindBy(xpath = "//span[contains(@class,'operation-success')]")
    private static WebElement successSnackbarMessage;

    @FindBy(xpath = "//span[contains(@class,'operation-error')]")
    private static WebElement errorSnackbarMessage;

    private String document1Path;
    private String document2Path;
    private String document3Path;
    private String document4Path;

    public DocumentCreationPage(String documentType){
        switch (documentType){
            case "INVOICE":
                document1Path= Paths.get("src/test/resources/documents/invoices/QATestInvoice1.tif").toAbsolutePath().toString();
                document2Path= Paths.get("src/test/resources/documents/invoices/QATestInvoice2.tif").toAbsolutePath().toString();
                document3Path= Paths.get("src/test/resources/documents/invoices/QATestInvoice3.tif").toAbsolutePath().toString();
                document4Path= Paths.get("src/test/resources/documents/invoices/QATestInvoice4.tif").toAbsolutePath().toString();
                break;
            case "BANK_STATEMENT":
                document1Path= Paths.get("src/test/resources/documents/bank_statements/QATestBankStatement1.jpg").toAbsolutePath().toString();
                document2Path= Paths.get("src/test/resources/documents/bank_statements/QATestBankStatement2.jpg").toAbsolutePath().toString();
                document3Path= Paths.get("src/test/resources/documents/bank_statements/QATestBankStatement3.png").toAbsolutePath().toString();
                document4Path= Paths.get("src/test/resources/documents/bank_statements/QATestBankStatement4.jpg").toAbsolutePath().toString();
                break;
            default:
                throw new RuntimeException("Invalid document type");
        }
        PageFactory.initElements(Base.driver,this);
    }

    /**
     * This method is used to simulate the action of uploading invoices.
     */
    public void uploadDocuments(int invoicesCount) {
        switch (invoicesCount) {
            case 1:
                fileInput.sendKeys(document1Path);
                break;
            case 2:
                fileInput.sendKeys(document1Path+"\n"+document2Path);
                break;
            case 3:
                fileInput.sendKeys(document1Path+"\n"+document2Path+"\n"+document3Path);
                break;
            case 4:
                fileInput.sendKeys(document1Path+"\n"+document2Path+"\n"+document3Path+"\n"+document4Path);
                break;
            default:
                log.error("Invalid invoices count");
                break;
        }
    }

    /**
     * This method is used to simulate the action of clicking the 'submit' button on the webpage.
     */
    public void clicksTheSubmitButton() {
        submitButton.click();
    }

    /**
     * This method is used to assert that some invoices were uploaded successfully.
     */
    public void successInvoiceUploadMessageDisplayedAssertion() {
        WebDriverWait wait=new WebDriverWait(Base.driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(successSnackbarMessage));
        Assert.assertEquals("All documents uploaded successfully!",successSnackbarMessage.getText());
    }

    /**
     * This method is used to assert that some invoices were uploaded successfully.
     */
    public void partialSuccessInvoiceUploadMessageDisplayedAssertion() {
        WebDriverWait wait=new WebDriverWait(Base.driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(errorSnackbarMessage));
        Assert.assertEquals("1 out of 4 documents uploaded successfully",errorSnackbarMessage.getText());
    }

}