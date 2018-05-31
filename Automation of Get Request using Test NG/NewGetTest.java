/*
 * @Author: Harsh Soni
 * 	
 * Demo of automating the get method of rest service
 * Idea behind this program is to give you a basic understanding how to validate Rest service's Get Method using TestNG
 * 
 * We will be verifying below parameters of Rest service Response
 * 
 * 1. Validating the Status code of Rest service
 * 2. Validating the header record of the Rest service
 * 3. Validating the actual JSON Body 
 * 
 * Prerequisite :
 * 					1. Download Rest Assured JAR
 * 					2. Install TestNG in Eclipse
 * 					3. Configure the JARs as well as xml for testNG in Eclipse
 * 
 */
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class NewGetTest {

	//Declaring constant of success status code of rest service
	//This constant will be used to verify the output
	private final int successStatusCode= 200;
	private final String contentTypeExpected ="application/json";
	private final String serverExpected = "nginx/1.12.2";
	private final String contentEncodingExpected = "gzip";
	private final String cityExpected = "amsterdam";
	
	//Declaring the global variable used across the class
	private RequestSpecification httpRequest;
	private Response response;
	private String responseBody;
	private JsonPath jsonPath;
	
	
	@BeforeSuite
	
	//Triggering the rest service as soon as Test suits start
	//Response is stored on response object so that the different test cases will validate different parameters in response
	public void callRestService()
	{
			//Specifying Rest Service URL
			RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
			
			//Getting the Request Specification of the request which will be send to the server
			httpRequest = RestAssured.given();
			
			//Below statement will actually hit the server using Get Method and store the response in response object
			response = httpRequest.request(Method.GET,"/amsterdam");
			
			//JSON response body will be stored in responseBody String variable
			responseBody = response.getBody().asString();
			
			//To validate the output of the node in response, we need to store the jsonPath
			jsonPath=response.jsonPath();
			
			//Reporting the output response using TESTNG Reporter
			Reporter.log(responseBody,true);			
	}
	
	//Test case 1: To verify the status code of the response
	@Test
	public void verifyStatusCode() 
	{
		
		//Getting the status code from the response
		//Success status code will always be 200
		int statusCode = response.getStatusCode();
		  
		//Asserting the status code, in case of failure the message written below will be displayed
		Assert.assertEquals(statusCode, successStatusCode,"Status code returned is incorrect");	  
	}  
	
	//Test case 2: Verifying the Headers
	@Test
	public void verifyHeaders()
	{
		//Getting content type from Response
		String contentType = response.header("Content-Type");
		Assert.assertEquals(contentType, contentTypeExpected, "Invalid Content Type");
		
		//Getting server type from Response
		String serverType =  response.header("Server");
		Assert.assertEquals(serverType, serverExpected,"Invalid Server Type");
		
		
		//Getting content encoding from Response
		String contentEncoding = response.header("Content-Encoding");
		Assert.assertEquals(contentEncoding, contentEncodingExpected,"Invalid Content Encoding");
	}
  
	//Test case 3: Verifying the JSON body
	@Test
	public void verifyResponseBody()
	{
		String city = jsonPath.getString("City").toLowerCase();
		Assert.assertEquals(city, cityExpected, "Invalid value in City");
	}
	
}
