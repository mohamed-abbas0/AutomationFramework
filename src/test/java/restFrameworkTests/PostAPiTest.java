package restFrameworkTests;

import Utilities.AutomationLogger;
import Utilities.CustomSoftAssert;
import Utilities.ExtentReportsHelpers.ExtentListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import RestFramework.Helpers.postHelper;
import RestFramework.Models.postAPI.postRequestModel;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PostAPiTest {
    postHelper postHelperObject;
    postRequestModel postRequest;
    ExtentListener extentListener;
    CustomSoftAssert softAssert;

    @BeforeClass (groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    public void beforeClass() throws IOException {
        postHelperObject = new postHelper();
        extentListener = new ExtentListener("APIAutomationTest");
        ThreadContext.put("Environment", System.getProperty("webUrl"));
        AutomationLogger.automationLogger.info("Running API Automation Test");
    }

    @BeforeMethod (groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    public void beforeMethodSetup(){
        softAssert = new CustomSoftAssert();
        postRequest = postHelperObject.setData();
    }

    @Test(priority = 1,groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Check that status code is 201 when using valid request")
    public void validGetPosts(){
        Response response = postHelperObject.postRequest(postRequest);
        postHelperObject.validTestAssertion(softAssert, response);
        softAssert.assertAll();
    }

    @AfterMethod(alwaysRun = true, groups = {"smoke_tests"})
    public void afterMethodTearDown(ITestResult result){
        extentListener.onTestStart(result);
        if (result.getStatus() == 1) {
            extentListener.onTestSuccess();
        }
        else if (result.getStatus() == 2) {
            extentListener.onTestFailureApi(softAssert);
        }
        else if (result.getStatus() == 3) {
            extentListener.onTestSkipped(softAssert);
        }
    }

    @AfterClass(alwaysRun = true, groups = {"smoke_tests"})
    public void afterClassTearDown(){
        extentListener.onFinish();
    }

}
