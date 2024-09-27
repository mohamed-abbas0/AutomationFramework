package org.restFramework.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.asserts.SoftAssert;
import org.restFramework.models.getAPI.getResponseModel;

import java.util.List;

import static org.restFramework.cosntants.Endpoints.*;
import static org.testng.Assert.assertEquals;

public class getHelper {

    public getHelper(){
        RestAssured.baseURI = baseURL;
        RestAssured.useRelaxedHTTPSValidation();
    }

    public Response getPosts() {
        return RestAssured
                .given().log().method().log().uri().log().body()
                .contentType(ContentType.JSON)
                .when()
                .get(posts)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public Response getPostWithId(String postId) {
        return RestAssured
                .given().log().method().log().uri().log().body()
                .contentType(ContentType.JSON)
                .pathParam("id", postId)
                .when()
                .get(postsWithId)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public Response getPostWithToken(String postId, String token) {
        return RestAssured
                .given().log().method().log().uri().log().body()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .pathParam("id",postId)
                .when()
                .get(postsWithId)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public Response getPostWithAuthentication(String username, String password) {
        return RestAssured
                .given().log().method().log().uri().log().body().auth().preemptive().basic(username, password)
                .contentType(ContentType.JSON)
                .when()
                .get(posts)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public void assertOnAllPosts(SoftAssert softAssert, Response response) throws JsonProcessingException {
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "Status code should be 200");
        ObjectMapper objectMapper = new ObjectMapper();
        List<getResponseModel> posts = objectMapper.readValue(response.asString(), new TypeReference<List<getResponseModel>>(){});
        for(int i = 0; i < posts.size(); i++) {
            softAssert.assertNotNull(posts.get(i).getUserId());
            softAssert.assertNotNull(posts.get(i).getId());
            softAssert.assertNotNull(posts.get(i).getTitle());
            softAssert.assertNotNull(posts.get(i).getBody());
        }
    }

    public void assertOnPostWithId(SoftAssert softAssert, Response response, String id) throws JsonProcessingException {
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "Status code should be 200");
        ObjectMapper objectMapper = new ObjectMapper();
        getResponseModel post = objectMapper.readValue(response.asString(), new TypeReference<getResponseModel>(){});
        softAssert.assertNotNull(post.getUserId());
        softAssert.assertNotNull(post.getId());
        softAssert.assertEquals(post.getId(), id);
        softAssert.assertNotNull(post.getTitle());
        softAssert.assertNotNull(post.getBody());

    }

    public void invalidTestAssertion(SoftAssert softAssert, Response response){
        assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Status code should be 400");
    }

}
