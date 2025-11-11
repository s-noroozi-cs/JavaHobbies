package com.example.demo;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class DemoApplicationTests {

  @LocalServerPort private int port;

  @Test
  void contextLoads() {}

  @Test
  void verifyActuator() throws Exception {
    var client = HttpClient.newBuilder().build();
    var uri = URI.create("http://localhost:" + port + "/actuator/health");
    var request = HttpRequest.newBuilder(uri).GET().build();
    var response = client.send(request, HttpResponse.BodyHandlers.ofString());

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertEquals("{\"status\":\"UP\"}", response.body());
  }

  @Test
  void verifySwaggerUi() throws Exception {
    var client = HttpClient.newBuilder().build();
    var uri = URI.create("http://localhost:" + port + "/swagger-ui.html");
    var request = HttpRequest.newBuilder(uri).GET().build();
    var response = client.send(request, HttpResponse.BodyHandlers.ofString());

    Assertions.assertEquals(302, response.statusCode());
    Assertions.assertEquals(
        "/swagger-ui/index.html",
            response.headers().allValues("location").getFirst());
  }
}
