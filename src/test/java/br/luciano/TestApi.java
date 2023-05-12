package br.luciano;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TestApi extends MassaDeDados {
	
	@BeforeClass
	public static void urlBase() {
		RestAssured.baseURI = "https://api.thecatapi.com/v1/";
	}
	
	public void validacao(Response response, int statusCode) {
		response.then().body("message", containsString("SUCCESS")).statusCode(statusCode);
		System.out.println("retorno => " + response.body().asPrettyString());
	}
	
	@Ignore
	@Test
	public void cadastro() {
		Response response =
		given()
			.contentType("application/json")
			.headers("x-api-key", apiKey)
			.body(bodyCadastro)
		.when()
			.post("user/passwordlesssignup");
		
		response.then()
			.statusCode(400);
		
		System.out.println("retorno => " + response.body().asString());
	}
	
	@Test
	public void getImage() {
		
		Response response =
		given()
			.contentType("application/json")
		.when()
				.get("images/0XYvRd7oD");
		
		response.then()
			.body("id", containsString("0XYvRd7oD"))
			.statusCode(200);
		
		System.out.println("retorno => " + response.body().asPrettyString());
	}
	
	@Test
	public void votes() {
		Response response =
				given()
					.contentType("application/json")
					.headers("x-api-key", apiKey)
					.body(bodyVotes)
				.when()
					.post("votes/");
		
		validacao(response,201);
		
		String id = response.jsonPath().getString("id");
		System.out.println("ID => " + id);
	}
	
	@Test
	public void deleteVotes() {
		Response response =
				given()
					.contentType("application/json")
					.headers("x-api-key", apiKey)
					.body(bodyDeleteVotes)
				.when()
					.post("votes/");
		
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
		Response response = 
				given()
					.contentType("application/json")
					.header("x-api-key", apiKey)
					.body(bodyFavourite)
				.when()
					.post("favourites/");
		validacao(response,200);
		
		return response.jsonPath().getString("id");
	}
	
	public boolean unFavourite(String id) {
		Response response = 
				given()
					.contentType("application/json")
					.header("x-api-key", apiKey)
					.pathParam("favouriteId", id)
				.when()
					.delete("favourites/{favouriteId}");
		validacao(response,200);
		
		return response.jsonPath().getString("message").equals("SUCCESS");
	}

}
