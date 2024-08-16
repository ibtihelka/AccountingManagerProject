package com.accounting_manager.accounting_manager.tests.auth;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends Base {

    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
    }

    @Test(description = "Successful Login")
    public void successfulLoginTest() {
        loginPage.entersValidUsernameAndPassword();
        loginPage.clicksTheLoginButton();
        loginPage.successfulLoginAssertion();
    }

    @Test(description = "Failed Login - Wrong Credentials")
    public void failedLoginWrongCredentialsTest() {
        loginPage.entersInvalidUsernameAndPassword();
        loginPage.clicksTheLoginButton();
        loginPage.errorSnackbarOperationMessageDisplayAssertion();
    }

    @Test(description = "Failed Login - Account not activated")
    public void failedLoginNotActivatedTest() {
        loginPage.entersNotActivatedAccountCredentials();
        loginPage.clicksTheLoginButton();
        loginPage.errorSnackbarOperationMessageDisplayAssertion();
    }
}