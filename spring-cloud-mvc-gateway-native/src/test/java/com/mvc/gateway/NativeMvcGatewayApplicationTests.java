package com.mvc.gateway;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NativeMvcGatewayApplicationTests {

  @LocalServerPort private int port;

  private ResponseUUIDMapper mapper = new ResponseUUIDMapper();

  @Test
  void contextLoads() {
    try {
		ResponseUUID uuid =
          HttpClient.newBuilder()
              .build()
              .sendAsync(
                  HttpRequest.newBuilder()
                      .GET()
                      .uri(URI.create("http://localhost:%d/uuid".formatted(port)))
                      .build(),
                  HttpResponse.BodyHandlers.ofString())
              .thenApply(HttpResponse::body)
              .thenApply(mapper::readValue)
				  .get();

      System.out.println(uuid);
		Assertions.assertFalse(uuid.uuid().isBlank());

    } catch (Throwable ex) {
      Assertions.fail(ex);
    }
  }
}

record ResponseUUID(String uuid) {}

class ResponseUUIDMapper extends com.fasterxml.jackson.databind.ObjectMapper {
  ResponseUUID readValue(String content) {
    try {
      return this.readValue(content, ResponseUUID.class);
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    }
  }
}
