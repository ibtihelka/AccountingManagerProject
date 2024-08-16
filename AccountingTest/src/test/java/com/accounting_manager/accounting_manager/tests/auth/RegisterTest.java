package com.accounting_manager.accounting_manager.tests.auth;

import com.accounting_manager.accounting_manager.page.auth.RegisterPage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterTest extends Base {

    private RegisterPage registerPage;

    @BeforeMethod
    public void setUp() {
        launchBrowser();
        registerPage = new RegisterPage();
        registerPage.loadUrl("registerRoute");
    }

    @Test(description = "Successful Physical Register")
    public void successfulPhysicalRegisterTest() {
        registerPage.entersValidPhysicalUserInformation();
        registerPage.clicksTheRegisterPhysicalButton();
        registerPage.successfulRegisterAssertion();
    }

    @Test(description = "Failed Register - Password Mismatch")
    public void failedRegisterPasswordMismatchTest() {
        registerPage.entersDifferentConfirmPassword();
        registerPage.disabledRegisterButtonAssertion();
    }

    @Test(description = "Successful Legal Register")
    public void successfulLegalRegisterTest() {
        registerPage.clicksTheLegalFormTab();
        registerPage.entersValidLegalUserInformation();
        registerPage.clicksTheRegisterLegalButton();
        registerPage.successfulRegisterAssertion();
    }
}
