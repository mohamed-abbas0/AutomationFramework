package RestFramework.Helpers;

import Utilities.CustomSoftAssert;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import Utilities.ExcelDataSheet;
import org.apache.http.HttpStatus;
import RestFramework.Models.postAPI.*;

import java.io.IOException;
import static RestFramework.Constants.Endpoints.*;

public class postHelper {

    postRequestModel postRequestModel;
    ExcelDataSheet excelDataSheet;

    public postHelper() throws IOException {
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
                .post(posts+"sad")
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

    public void validTestAssertion(CustomSoftAssert softAssert, Response response){
        //softAssert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "Status code should be 201");
        softAssert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "Status code should be 201");
        softAssert.assertNotNull(response.jsonPath().getJsonObject("userId"),"userId is null");
        softAssert.assertNotNull(response.jsonPath().getJsonObject("id"), "id is null");
        softAssert.assertNotNull(response.jsonPath().getJsonObject("body"), "body is null");
        softAssert.assertNotNull(response.jsonPath().getJsonObject("title"), "title is null");
    }
}
