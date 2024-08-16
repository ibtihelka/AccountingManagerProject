package com.accounting_manager.accounting_manager.page.document.bank_statement;

import com.accounting_manager.accounting_manager.page.document.DocumentUpdatePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BankStatementUpdatePage extends DocumentUpdatePage {

    public BankStatementUpdatePage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Bank Statement Form Elements
     */
    @FindBy(id = "name")
    private static WebElement bankStatementNameBox;
    @FindBy(id = "accountNumber")
    private static WebElement accountNumberBox;
    @FindBy(id = "rib")
    private static WebElement ribNumberBox;
    @FindBy(id = "iban")
    private static WebElement ibanNumberBox;
    @FindBy(id = "bic")
    private static WebElement bicNumberBox;
    @FindBy(id = "bankStatementDate")
    private static WebElement bankStatementDateBox;
    @FindBy(id = "periodStartDate")
    private static WebElement periodStartDateBox;
    @FindBy(id = "periodEndDate")
    private static WebElement periodEndDateBox;

    /**
     * This method is used to simulate the action of updating the bank statement information on the webpage.
     */
    @Override
    public void updateDocumentInformation(){
        bankStatementNameBox.sendKeys("QATestBankStatement0045");
        accountNumberBox.sendKeys("12345678901");
        ibanNumberBox.sendKeys("FR1234567890123456789012345");
        ribNumberBox.sendKeys("1234567890123456789012345");
        bicNumberBox.sendKeys("ABCDEFGHXXX");
        bankStatementDateBox.sendKeys("01/02/1980");
        periodStartDateBox.sendKeys("01/01/1980");
        periodEndDateBox.sendKeys("15/01/1980");
    }

}