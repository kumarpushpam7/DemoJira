import static io.restassured.RestAssured.*;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

public class OAuthTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		/*System.setProperty("webdriver.chrome.driver", "C://Users//Lenovo//Downloads//chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		//getting Authorization code
	    driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=abcdefgh");
		driver.findElement(By.xpath("input[@type='email']")).sendKeys("kumarpushpam6569");
		driver.findElement(By.xpath("input[@type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(30000);
		driver.findElement(By.xpath("input[@type='password']")).sendKeys("kumar.pushpam7");
		driver.findElement(By.xpath("input[@type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(40000);*/
		String url = "https://rahulshettyacademy.com/getCourse.php?state=abcdefgh&code=4%2F1wH57vjmFAU7Zw-eOJNS9KoakTHZKl9ufNotVP716KHa22pwtdqAenLlYcCPMoGEzb6QmpoYpxEYablOqfvL7jA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none"; 
				
		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0]; //will have authorization code which we require
		
		System.out.println(code);
		
		//getting Access Token
	   String accessTokenResponse = given().urlEncodingEnabled(false).queryParams("code",code)
			                               .queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		                                   .queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		                                   .queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		                                   .queryParams("grant_type","authorization_code")
		                                   .queryParams("state", "verifyfjdss")
		                                   .queryParams("session_state", "ff4a89d1f7011eb34eef8cf02ce4353316d9744b..7eb8")
   		                            .when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();//we can skip .extract().response().asString()
	   
	   JsonPath js  = new JsonPath(accessTokenResponse);
	   String accessToken = js.getString("access_token");
		
	   //sending actual request using deserialization concept(POJO class)
	   GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		                 .when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
	  // System.out.println(response);
	   
	   //getting linkedIn value
	   System.out.println(gc.getLinkedIn());
	   //getting instructor value
	   System.out.println(gc.getInstructor());
	   
	   //getting Price of SOAPUI webservices testing
	   List<Api> api = gc.getCourses().getApi();
	   
	   for(int i=0; i<api.size(); i++){
		   
		  if(api.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")){
			  
			 System.out.println(api.get(i).getPrice());
		  }
	   }
	   
	   //getting all courseTitles in WebAutomation
	  List<WebAutomation> webAutomation = gc.getCourses().getWebAutomation();
	  
	  for(int i=0; i<webAutomation.size(); i++){
		  
		  
		  System.out.println(webAutomation.get(i).getCourseTitle());
	  }
	}

}
