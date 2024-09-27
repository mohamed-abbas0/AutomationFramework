package org.restFrameworkTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.Utils.ExcelDataSheet;
import org.restFramework.helpers.getHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.FileNotFoundException;

public class getTest {
    getHelper getHelperObject = new getHelper();
    ExcelDataSheet excelDataSheet;
    SoftAssert softAssert;

    @BeforeMethod(groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    public void beforeMethodSetup() throws FileNotFoundException {
       excelDataSheet = new ExcelDataSheet("./data/Test Data.xlsx", "Sheet1");
       softAssert = new SoftAssert();
    }

    @Test (priority = 1,groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Check that status code is 200 when using valid request")
    public void validGetPosts() throws JsonProcessingException {
        Response response = getHelperObject.getPosts();
        getHelperObject.assertOnAllPosts(softAssert, response);
        softAssert.assertAll();
    }

    @Test (priority = 1,groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Check that status code is 200 when using valid request")
    public void validGetPostWithId() throws JsonProcessingException {
        String postId = excelDataSheet.getCellValue(excelDataSheet.getRowIndex("valid"),
                excelDataSheet.getColumnIndex("id"));
        Response response = getHelperObject.getPostWithId(postId);
        getHelperObject.assertOnPostWithId(softAssert, response, postId);
        softAssert.assertAll();
    }
}
