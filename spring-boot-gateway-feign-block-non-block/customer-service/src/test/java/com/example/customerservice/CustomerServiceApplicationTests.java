package com.example.customerservice;

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
class CustomerServiceApplicationTests {


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
	@SneakyThrows
	void check_open_api_json() {
		HttpResponse<String> response = sendGetRequest("/v3/api-docs");
		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

		JSONObject js = new JSONObject(response.body());
		JSONObject paths = js.getJSONObject("paths");

		String specificMerchantPath = "/api/v1/customers/{customer-id}";
		Assertions.assertTrue(paths.has(specificMerchantPath));
		check_operation_status(paths.getJSONObject(specificMerchantPath),true);


		String saveMerchantPath = "/api/v1/customers";
		Assertions.assertTrue(paths.has(saveMerchantPath));
		check_operation_status(paths.getJSONObject(saveMerchantPath),false);

	}

	@Test
	void check_swagger_ui_redirect(){
		HttpResponse<String> response = sendGetRequest("/swagger-ui.html");
		Assertions.assertEquals(HttpStatus.FOUND.value(), response.statusCode());
	}

	@Test
	void check_swagger_ui(){
		HttpResponse<String> response = sendGetRequest("/swagger-ui/index.html");
		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
	}

	private void check_operation_status(JSONObject operation,boolean existMerchant){
		Assertions.assertEquals(existMerchant,operation.has("get"));
		Assertions.assertEquals(existMerchant,operation.has("put"));
		Assertions.assertEquals(existMerchant,operation.has("delete"));
		Assertions.assertNotEquals(existMerchant,operation.has("post"));
	}

}
