package com.accounting_manager.accounting_manager.page.document.invoice;

import com.accounting_manager.accounting_manager.page.document.DocumentUpdatePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class InvoiceUpdatePage extends DocumentUpdatePage {

    public InvoiceUpdatePage(){
        PageFactory.initElements(driver,this);
    }

    /**
     * Invoice Form Elements
     */
    @FindBy(id = "name")
    private static WebElement invoiceNameBox;
    @FindBy(id = "supplierNumber")
    private static WebElement supplierNumberBox;
    @FindBy(id = "pagesNumber")
    private static WebElement pagesNumberBox;
    @FindBy(id = "siretNumber")
    private static WebElement siretNumberBox;
    @FindBy(id = "invoiceNumber")
    private static WebElement invoiceNumberBox;
    @FindBy(id = "tvaNumber")
    private static WebElement tvaNumberBox;
    @FindBy(id = "invoiceDate")
    private static WebElement invoiceDateBox;
    @FindBy(id = "tvaNumber")
    private static WebElement tvaBox;
    @FindBy(id = "tvaNumber")
    private static WebElement htBox;
    @FindBy(id = "tvaNumber")
    private static WebElement ttcBox;
    @FindBy(id = "tvaNumber")
    private static WebElement supportTvaBox;
    @FindBy(id = "tvaNumber")
    private static WebElement discountBox;

    /**
     * This method is used to simulate the action of updating the invoice information on the webpage.
     */
    @Override
    public void updateDocumentInformation(){
        invoiceNameBox.sendKeys("QATestInvoice0045");
        supplierNumberBox.sendKeys("QATestInvoice0045");
        pagesNumberBox.sendKeys("1");
        siretNumberBox.sendKeys("12345678901234");
        invoiceNumberBox.sendKeys("123456");
        tvaNumberBox.sendKeys("123456");
        invoiceDateBox.sendKeys("01/01/1960");
        tvaBox.sendKeys("20");
        htBox.sendKeys("1000");
        ttcBox.sendKeys("1200");
        supportTvaBox.sendKeys("200");
        discountBox.sendKeys("0");
    }

}