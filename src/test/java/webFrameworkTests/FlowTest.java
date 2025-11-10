package webFrameworkTests;

import Utilities.AutomationLogger;
import Utilities.CustomSoftAssert;
import Utilities.ExtentReportsHelpers.ExtentListener;
import com.microsoft.playwright.*;
import io.qameta.allure.Description;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import WebFramework.Pages.CartPage;
import WebFramework.Pages.HomePage;
import WebFramework.Pages.ItemPage;

import java.awt.*;
import java.util.List;

import static Configurations.Configurations.webBrowser;
import static Configurations.Configurations.webUrl;
import static WebFramework.Constants.HomePageElementLocators.firstItem;
import static WebFramework.Constants.ItemPageElementLocators.viewCartBtn;

public class FlowTest {
    Browser browser;
    BrowserContext context;
    Page page;
    ExtentListener extentListener;
    CustomSoftAssert softAssert;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();
    HomePage homePage;
    ItemPage itemPage;
    CartPage cartPage;

    @BeforeClass(groups = {"smoke_tests"})
    public void beforeClassSetup(){
        switch (webBrowser) {
            case "Chrome" ->
                    browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(List.of("--start-maximized")));
            case "Edge" ->
                    browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false).setArgs(List.of("--start-maximized")));
            case "Firefox" ->
                    browser = Playwright.create().firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(List.of("--width=" + width, "--height=" + height)));
        }
        extentListener = new ExtentListener("WebAutomationTest");
        ThreadContext.put("Environment", System.getProperty("webUrl"));
        AutomationLogger.automationLogger.info("Running Automation Test");
        homePage = new HomePage();
        itemPage = new ItemPage();
        cartPage = new CartPage();
    }

    @BeforeMethod(groups = {"smoke_tests"})
    public void beforeMethodSetup(){
        softAssert = new CustomSoftAssert();
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions().setIgnoreHTTPSErrors(true);
        if (webBrowser.equalsIgnoreCase("Firefox"))
            contextOptions.setViewportSize(width, height);
        else
            contextOptions.setViewportSize(null);
        context = browser.newContext(contextOptions);
        page = context.newPage();
        try {
            page.navigate(webUrl);
        }
        catch (PlaywrightException e) {
            AutomationLogger.automationLogger.error("Error ", e);
            softAssert.assertTrue(false, "Navigation Error, Cannot Open Requested URL");
            softAssert.assertAll();
        }
    }

    @Test(groups = {"smoke_tests"})
    @Description("Check that selected item is added to cart successfully")
    public void addItemToCart(){
        try {
            homePage.searchForItem(page, "iPad");
            homePage.sortItems(page, "PriceHighLow");
            page.click(firstItem);
            itemPage.saveItemDataAndAddToCart(page);
            page.click(viewCartBtn);
            cartPage.assertOnItemData(page, itemPage.getItemName(), itemPage.getItemPrice(), softAssert);
            softAssert.assertAll();
        }
        catch (AssertionError e){
            AutomationLogger.automationLogger.error("Error ", e);
            softAssert.assertAll();
        }
        catch (PlaywrightException e) {
            AutomationLogger.automationLogger.error("Error ", e);
            softAssert.assertTrue(false, "TEST CASE TERMINATED [web element was not found], Check log file.");
            softAssert.assertAll();
        }
    }

    @AfterMethod(alwaysRun = true, groups = {"smoke_tests"})
    public void afterMethodTearDown(ITestResult result){
        extentListener.onTestStart(result);
        if (result.getStatus() == 1) {
            extentListener.onTestSuccess();
        }
        else if (result.getStatus() == 2) {
            extentListener.onTestFailureWeb(result, page, softAssert);
        }
        else if (result.getStatus() == 3) {
            extentListener.onTestSkipped(softAssert);
        }
        page.close();
        context.close();
    }

    @AfterClass(alwaysRun = true, groups = {"smoke_tests"})
    public void afterClassTearDown(){
        extentListener.onFinish();
        browser.close();
    }
}
