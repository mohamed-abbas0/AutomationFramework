package Utilities.ExtentReportsHelpers;

import Utilities.CustomSoftAssert;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.microsoft.playwright.Page;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ExtentListener implements ITestListener {

    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String date = currentDate.format(formatter).replaceAll(":", "-");
    private static ExtentReports extent;
    public static ExtentTest testReport;

    public ExtentListener(String testClassName){
        new File(".\\Reports\\" + date.substring(0, 10)).mkdir();
        extent = ExtentManager.createInstance(".\\Reports\\" + date.substring(0, 10) + "\\" + testClassName + " " + date.substring(11, 19)  + ".html");
    }

    public void onTestStart(ITestResult result) {
        testReport = extent.createTest(result.getMethod().getMethodName());
        testReport.info("Test case started");
    }

    public void onTestSuccess() {
        String logText = "TEST CASE PASSED";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        testReport.pass(m);
    }

    public void onTestFailureApi(CustomSoftAssert softAssert) {
        if (!softAssert.getErrorMessages().isEmpty())
        {
            testReport.fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Error Occurred: Click to see"
                    + "</font>" + "</b >" + "</summary>" + softAssert.getErrorMessages().toString().replaceAll(",", "<br>") + "</details>" + " \n");
        }
        String failureLog = "TEST CASE FAILED";
        Markup m = MarkupHelper.createLabel(failureLog, ExtentColor.RED);
        testReport.log(Status.FAIL, m);
    }

    public void onTestFailureWeb(ITestResult result, Page page, CustomSoftAssert softAssert) {
      if (!softAssert.getErrorMessages().isEmpty())
        {
            testReport.fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Error Occurred: Click to see"
                    + "</font>" + "</b >" + "</summary>" + softAssert.getErrorMessages().toString()/*.replaceAll(",", "<br>")*/ + "</details>" + " \n");
        }
        byte[] screenshotBytes = ExtentManager.captureScreenshot(result, page, date);
        String base64Screenshot = Base64.getEncoder().encodeToString(screenshotBytes);
        testReport.fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        String failureLog = "TEST CASE FAILED";
        Markup m = MarkupHelper.createLabel(failureLog, ExtentColor.RED);
        testReport.log(Status.FAIL, m);
    }

    public void onTestSkipped(CustomSoftAssert softAssert) {
        if (!softAssert.getErrorMessages().isEmpty())
        {
            testReport.skip("<details>" + "<summary>" + "<b>" + "<font color=" + "yellow>" + "Error Occurred: Click to see"
                    + "</font>" + "</b >" + "</summary>" + softAssert.getErrorMessages().toString()/*.replaceAll(",", "<br>")*/ + "</details>" + " \n");
        }
        String logText = "TEST CASE SKIPPED";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
        testReport.skip(m);
    }

    public void onFinish() {
        if (extent != null)
            extent.flush();
    }
}