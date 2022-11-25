package com.example.gateway;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayApplicationTests {
	@LocalServerPort
	private int port;

	@SneakyThrows
	HttpResponse<String> sendGetRequest(String path) {
		URI uri = new URI("http://localhost:" + port + path);
		HttpRequest request = HttpRequest
				.newBuilder(uri)
				.GET()
				.build();
		return HttpClient.newBuilder()
				.build()
				.send(request, HttpResponse.BodyHandlers.ofString());
	}


	@Test
	void contextLoads() {
	}

	@Test
	void check_swagger_ui_redirect(){
		HttpResponse<String> response = sendGetRequest("/swagger-ui.html");
		Assertions.assertEquals(HttpStatus.FOUND.value(), response.statusCode());
	}

	@Test
	void check_swagger_ui(){
		HttpResponse<String> response = sendGetRequest("/webjars/swagger-ui/index.html");
		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
	}

}
