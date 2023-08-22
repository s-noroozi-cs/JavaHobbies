package com.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultipartRestApiAppTests {

  @LocalServerPort private int port;

  @Test
  void context_load() {
    Assertions.assertTrue(port > 0);
  }

  private URI getUploadUri() {
    return URI.create("http://localhost:%d/upload".formatted(port));
  }

  @Test
  void invoke_empty_body_post() throws Exception {
    Path file =
        Path.of(
            Thread.currentThread()
                .getContextClassLoader()
                .getResource("application.properties")
                .toURI());
    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder(getUploadUri())
                    .POST(HttpRequest.BodyPublishers.ofFile(file))
                    .build(),
                HttpResponse.BodyHandlers.ofString());
    Assertions.assertEquals("", response.body());
    Assertions.assertEquals(200, response.statusCode());
  }
}
