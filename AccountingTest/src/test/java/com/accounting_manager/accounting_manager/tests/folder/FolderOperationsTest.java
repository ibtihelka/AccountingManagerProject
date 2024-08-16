package com.accounting_manager.accounting_manager.tests.folder;

import com.accounting_manager.accounting_manager.page.auth.LoginPage;
import com.accounting_manager.accounting_manager.page.folder.FolderOperationsPage;
import com.accounting_manager.accounting_manager.utils.Base;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FolderOperationsTest extends Base {

    private FolderOperationsPage folderOperationsPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        launchBrowser();
        loginPage = new LoginPage();
        loginPage.loadUrl("loginRoute");
        loginPage.performLogin();
        folderOperationsPage = new FolderOperationsPage();
    }

    @Test(description = "Successful Folder Mark As Favorite", priority = 1)
    public void testSuccessfulFolderMarkAsFavorite() {
        folderOperationsPage.loadUrl("manageFoldersRoute");
        String selectedFolderName = folderOperationsPage.clicksTheToolMenuOfTheFirstDisplayedFolder();
        folderOperationsPage.clicksTheAddToFavoriteButton(selectedFolderName);
        folderOperationsPage.successSnackbarOperationMessageDisplayAssertion();
        folderOperationsPage.loadUrl("manageFoldersRoute", "manageFoldersRoute.queries.favorite");
        folderOperationsPage.searchFolderByName(selectedFolderName);
        folderOperationsPage.folderAppearInInterfaceAssertion(selectedFolderName);
    }

    @Test(description = "Successful Folder Unmark From Favorites", priority = 2)
    public void testSuccessfulFolderUnmarkFromFavorites() {
        folderOperationsPage.loadUrl("manageFoldersRoute", "manageFoldersRoute.queries.favorite");
        String selectedFolderName = folderOperationsPage.clicksTheToolMenuOfTheFirstDisplayedFolder();
        folderOperationsPage.clicksTheRemoveFromFavoriteButton();
        folderOperationsPage.successSnackbarOperationMessageDisplayAssertion();
        folderOperationsPage.searchFolderByName(selectedFolderName);
        folderOperationsPage.folderDisappearedFromInterfaceAssertion();
    }

    @Test(description = "Successful Folder Archive", priority = 3)
    public void testSuccessfulFolderArchive() {
        folderOperationsPage.loadUrl("manageFoldersRoute");
        String selectedFolderName = folderOperationsPage.clicksTheToolMenuOfTheFirstDisplayedFolder();
        folderOperationsPage.clicksTheArchiveButton();
        folderOperationsPage.successSnackbarOperationMessageDisplayAssertion();
        folderOperationsPage.loadUrl("manageFoldersRoute", "manageFoldersRoute.queries.archive");
        folderOperationsPage.folderAppearInInterfaceAssertion(selectedFolderName);
    }

    @Test(description = "Successful Folder Unarchive", priority = 4)
    public void testSuccessfulFolderUnarchive() {
        folderOperationsPage.loadUrl("manageFoldersRoute", "manageFoldersRoute.queries.archive");
        String selectedFolderName = folderOperationsPage.clicksTheToolMenuOfTheFirstDisplayedFolder();
        folderOperationsPage.clicksTheUnarchiveButton();
        folderOperationsPage.successSnackbarOperationMessageDisplayAssertion();
        folderOperationsPage.searchFolderByName(selectedFolderName);
        folderOperationsPage.folderDisappearedFromInterfaceAssertion();
    }
}
