//package com.example.demo;
//
//import lombok.SneakyThrows;
//import org.json.JSONObject;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class DemoApplicationTests {
//
//	@LocalServerPort
//	private int port;
//
//	@Test
//	void contextLoads() {
//	}
//
//	@SneakyThrows
//	private URI makeRequestPath(String path){
//		return new URI("http://localhost:" + port + path);
//	}
//
//	@SneakyThrows
//	private HttpResponse<String> sendGetRequest(String url){
//		HttpRequest request = HttpRequest
//				.newBuilder(makeRequestPath(url))
//				.GET()
//				.build();
//		return HttpClient
//				.newBuilder()
//				.build()
//				.send(request, HttpResponse.BodyHandlers.ofString());
//	}
//
//	@Test
//	@SneakyThrows
//	void fetch_open_api_doc(){
//		HttpResponse<String> response = sendGetRequest("/v3/api-docs");
//		Assertions.assertEquals(200,response.statusCode());
//		JSONObject js = new JSONObject(response.body());
//		String openApiVersion = js.getString("openapi");
//		Assertions.assertEquals("3.0.1",openApiVersion);
//	}
//
//	@Test
//	void swagger_ui_page(){
//		HttpResponse<String> response = sendGetRequest("/swagger-ui.html");
//		Assertions.assertEquals(302,response.statusCode());
//		String location = response.headers().firstValue("location").get();
//		response = sendGetRequest(location);
//		Assertions.assertEquals(200,response.statusCode());
//	}
//
//}
