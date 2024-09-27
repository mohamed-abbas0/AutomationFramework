package org.webFrameworkTests;

import com.microsoft.playwright.*;
import io.qameta.allure.Description;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import org.webFramework.helpers.newAccountPageHelper;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.webFramework.cosntants.ElementLocators.*;
import static org.webFramework.cosntants.WebTestData.webBrowser;
import static org.webFramework.cosntants.WebTestData.webUrl;

public class newAccountTest {
    private Browser browser;
    private BrowserContext context;
    private Page page;
    SoftAssert softAssert;
    String timeStamp;
    newAccountPageHelper newAccountPageHelper;

    @BeforeClass(groups = {"smoke_tests", "valid_test", "invalid_tests", "full_regression_tests", "regression_without_card_statuses_tests"})
    public void beforeClassSetUp() {
        //Create a new folder in each run to save screenshots
        timeStamp = new SimpleDateFormat("(dd-MM-yyyy HH-mm-ss)").format(Calendar.getInstance().getTime());
        new File(".\\Screenshots\\" + timeStamp).mkdir();
        if (webBrowser.equals("Chrome")) {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(300));
        } else if (webBrowser.equals("Firefox")) {
            browser = Playwright.create().firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(300));
        }
        newAccountPageHelper = new newAccountPageHelper();
    }

    @BeforeMethod(groups = {"smoke_tests", "valid_test", "invalid_tests", "full_regression_tests", "regression_without_card_statuses_tests"})
    public void beforeMethodSetUp() {
        context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
        softAssert = new SoftAssert();
        page = context.newPage();
    }

    @Test(priority = 1, groups = {"invalid_tests", "full_regression_tests", "regression_without_card_statuses_tests"})
    @Description("Check that expired token screen elements (style - text - font) is displayed correctly in arabic language")
    public void test() {
        page.navigate(webUrl);
        page.hover(account_listsBtn);
        page.click(newAccountBtn);
        page.waitForSelector(createAccountTitle);
        ElementHandle createAccountTitleHandle = page.querySelector(createAccountTitle);
        newAccountPageHelper.assertOnFont("Create Account Title", createAccountTitleHandle, softAssert);
        softAssert.assertAll();
    }

    @AfterMethod(groups = {"smoke_tests", "valid_test", "invalid_tests", "full_regression_tests", "regression_without_card_statuses_tests"})
    public void afterMethodTearDown(ITestResult result) {
        if (result.getStatus() == 1) {
            String path = ".\\Screenshots\\" + timeStamp + "\\Passed\\" + result.getName() + " test screenshot.png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
        } else {
            String path = ".\\Screenshots\\" + timeStamp + "\\Failed\\" + result.getName() + " test screenshot.png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
        }
        page.close();
    }

    @AfterClass(groups = {"smoke_tests", "valid_test", "invalid_tests", "full_regression_tests", "regression_without_card_statuses_tests"})
    public void afterClassTearDown() {
        context.close();
        browser.close();
    }
}
