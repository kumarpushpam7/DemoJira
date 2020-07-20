import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;


public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestAssured.baseURI = "http://localhost:8080";
		
		//Login Scenario
		SessionFilter session = new SessionFilter();
		String response = given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{ \"username\": \"pushpamkumar6569\", \"password\": \"Kumar.pushpam8060\" }").log().all()
		.filter(session).when().post("/rest/auth/1/session")
		.then().log().all().extract().response().asString();
		
		String expectedMessage = "Hi, how are you?";
		//Adding Comment
		String addCommentResponse = given().pathParam("ID", "10002").header("Content-Type","application/json").body("{\r\n" + 
				"    \"body\": \""+expectedMessage+"\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}")
		.filter(session).when().post("/rest/api/2/issue/{ID}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();
         
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");
		
		//Adding Attachment
		given().pathParam("ID", "10002").header("X-Atlassian-Token","no-check").filter(session).header("Content-Type","multipart/form-data")
		.multiPart("file",new File("jira.txt"))
		.when().post("/rest/api/2/issue/{ID}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		
		//Get Issue
		String issueDetails = given().filter(session).pathParam("ID", "10002").queryParam("fields", "comment").log().all()
		.when().get("/rest/api/2/issue/{ID}")
		.then().log().all().extract().response().asString();
		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentCounts = js1.getInt("fields.comment.comments.size()");
		
		//getting comment body for given comment id AND comparing if given message is same as message we extract
		for(int i=0; i<commentCounts; i++){
			
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equalsIgnoreCase(commentId)){
				
			String message = js1.get("fields.comment.comments["+i+"].body").toString();
			System.out.println(message);
			Assert.assertEquals(message, expectedMessage);
				
			}
		}
		
		
		
	}

}
