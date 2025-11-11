package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class DemoApplicationTests {

    @LocalServerPort
    private int port;

	@Test
	void contextLoads() {
	}

    @Test
    void verifySwaggerUi()throws Exception{
        var client = HttpClient.newBuilder().build();
        var uri = URI.create("http://localhost:"+port+"/swagger-ui.html");
        var request = HttpRequest.newBuilder(uri).GET().build();
        var response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

    Assertions.assertEquals(404, response.statusCode());
    }

}
