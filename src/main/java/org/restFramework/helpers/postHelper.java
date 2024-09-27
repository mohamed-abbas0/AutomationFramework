package org.restFramework.helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.Utils.ExcelDataSheet;
import org.apache.http.HttpStatus;
import org.testng.asserts.SoftAssert;
import org.restFramework.models.postAPI.*;

import java.io.FileNotFoundException;

import static org.restFramework.cosntants.Endpoints.*;
import static org.testng.Assert.assertEquals;

public class postHelper {

    postRequestModel postRequestModel;
    ExcelDataSheet excelDataSheet;

    public postHelper() throws FileNotFoundException {
        RestAssured.baseURI = baseURL;
        RestAssured.useRelaxedHTTPSValidation();
        excelDataSheet = new ExcelDataSheet("./data/Test Data.xlsx", "Sheet1");
    }

    public postRequestModel setData(){
        postRequestModel = new postRequestModel();
        postRequestModel.setUserId(excelDataSheet.getCellValue(excelDataSheet.getRowIndex("valid"),
                excelDataSheet.getColumnIndex("userId")));
        postRequestModel.setBody(excelDataSheet.getCellValue(excelDataSheet.getRowIndex("valid"),
                excelDataSheet.getColumnIndex("body")));
        postRequestModel.setTitle(excelDataSheet.getCellValue(excelDataSheet.getRowIndex("valid"),
                excelDataSheet.getColumnIndex("title")));
        return postRequestModel;
    }

    public Response postRequest(postRequestModel postRequestModel) {
        return RestAssured
                .given().log().method().log().uri().log().body()
                .contentType(ContentType.JSON)
                .body(postRequestModel)
                .when()
                .post(posts)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public Response getPostWithAuthentication(postRequestModel postRequestModel, String username, String password) {
        return RestAssured
                .given().log().method().log().uri().log().body().auth().preemptive().basic(username, password)
                .contentType(ContentType.JSON)
                .body(postRequestModel)
                .when()
                .post(posts)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public void validTestAssertion(SoftAssert softAssert, Response response){
        assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "Status code should be 201");
        softAssert.assertNotNull(response.jsonPath().getJsonObject("userId"));
        softAssert.assertNotNull(response.jsonPath().getJsonObject("id"));
        softAssert.assertNotNull(response.jsonPath().getJsonObject("body"));
        softAssert.assertNotNull(response.jsonPath().getJsonObject("title"));
    }
}
