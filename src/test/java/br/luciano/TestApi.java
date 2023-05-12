package br.luciano;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.response.Response;

public class TestApi {
	private String apiKey = "live_WI3i3k5hG7CnKI9wRnGlMWMlKHl0upQ5YxNpG4n8lXLPSmGfzs35g4SQ9zWCx8we";
	
	@Ignore
	@Test
	public void cadastro() {
		String url = "https://api.thecatapi.com/v1/user/passwordlesssignup";
		String body = "{\"email\": \"emaild@gmail.com\",\"appDescription\": \"The cat API\"}";
		Response response =
		given()
			.contentType("application/json")
			.headers("x-api-key", apiKey)
			.body(body)
		.when()
			.post(url);
		
		response.then()
			.statusCode(400);
		
		System.out.println("retorno: " + response.body().asString());
	}
	
	@Test
	public void getImage() {
		String url = "https://api.thecatapi.com/v1/images/0XYvRd7oD";
		Response response =
		given()
			.contentType("application/json")
		.when()
				.get(url);
		
		response.then()
			.body("id", containsString("0XYvRd7oD"))
			.statusCode(200);
		
		System.out.println("retorno => " + response.body().asPrettyString());
	}
	
	@Test
	public void votes() {
		String url = "https://api.thecatapi.com/v1/votes/";
		String body = "{\"image_id\": \"2dt\", \"value\": \"true\", \"sub_id\": \"demo-ce7196\"}";
		Response response =
				given()
					.contentType("application/json")
					.headers("x-api-key", apiKey)
					.body(body)
				.when()
					.post(url);
		
		response.then()
			.body("message", containsString("SUCCESS"))
			.statusCode(201);
		
		System.out.println("retorno => " + response.body().asPrettyString());
		String id = response.jsonPath().getString("id");
		System.out.println("ID => " + id);
	}
	
	@Test
	public void deleteVotes() {
		String url = "https://api.thecatapi.com/v1/votes/";
		String body = "{\"image_id\": \"2dt\", \"value\": \"false\", \"sub_id\": \"demo-ce7196\"}";
		Response response =
				given()
					.contentType("application/json")
					.headers("x-api-key", apiKey)
					.body(body)
				.when()
					.post(url);
		
		response.then()
			.body("message", containsString("SUCCESS"))
			.body("image_id", containsString("2dt"))
		.statusCode(201);
		
		System.out.println("retorno => " + response.body().asPrettyString());
		String id = response.jsonPath().getString("id");
		System.out.println("ID => " + id);
	}
	
	@Test
	public void favourites() {
		String id =	favourite();
		Assert.assertTrue(unFavourite(id));
	}
	
	public String favourite() {
		String url = "https://api.thecatapi.com/v1/favourites/";
		String body = "{\"image_id\": \"b3v\"}";
		Response response = 
				given()
					.contentType("application/json")
					.header("x-api-key", apiKey)
					.body(body)
				.when()
					.post(url);
		response.then()
			.statusCode(200);
		
		System.out.println("retorno => " + response.body().asPrettyString());
		return response.jsonPath().getString("id");
	}
	
	public boolean unFavourite(String id) {
		String url = "https://api.thecatapi.com/v1/favourites/{favouriteId}";
		Response response = 
				given()
					.contentType("application/json")
					.header("x-api-key", apiKey)
					.pathParam("favouriteId", id)
				.when()
					.delete(url);
		response.then()
			.statusCode(200);
		
		System.out.println("retorno => " + response.body().asPrettyString());
		return response.jsonPath().getString("message").equals("SUCCESS");
	}

}
