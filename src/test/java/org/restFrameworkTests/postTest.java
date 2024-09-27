package org.restFrameworkTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.restFramework.helpers.postHelper;
import org.restFramework.models.postAPI.postRequestModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.FileNotFoundException;

public class postTest {
    postHelper postHelperObject;
    SoftAssert softAssert;
    postRequestModel postRequest;

    @BeforeClass (groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    public void beforeClass() throws FileNotFoundException {
        postHelperObject = new postHelper();
    }

    @BeforeMethod (groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    public void beforeMethodSetup() throws FileNotFoundException {
        softAssert = new SoftAssert();
        postRequest = postHelperObject.setData();
    }

    @Test(priority = 1,groups = {"smoke_tests", "valid_tests", "full_regression_tests"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Check that status code is 201 when using valid request")
    public void validGetPosts() throws JsonProcessingException {
        Response response = postHelperObject.postRequest(postRequest);
        postHelperObject.validTestAssertion(softAssert, response);
        softAssert.assertAll();
    }

}
