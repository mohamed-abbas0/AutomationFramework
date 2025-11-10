package Utilities.ExtentReportsHelpers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Paths;

public class ExtentManager {

    public static ExtentReports createInstance(String fileName) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);

        //sparkReporter.config().setChartVisibilityOnOpen(true);
        //sparkReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle(fileName);
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setReportName(fileName);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Automation Tester", "Mohamed Abbas");
        extent.setSystemInfo("Organization", "TEST");
        return extent;
    }

    public static String screenshotName;
    public static byte[] captureScreenshot(ITestResult result, Page page, String date) {
        String path = "./Screenshots/Failed " + date.substring(0, 10) + "/" + date.substring(11, 19);
        new File(path).mkdir();
        screenshotName = path + "/" + result.getName() + " test screenshot.png";
        page.waitForTimeout(1000);
        return page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotName)));
    }
}
