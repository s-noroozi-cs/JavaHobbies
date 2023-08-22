package com.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
  void invoke_upload_file() {

    HttpHeaders headers = new HttpHeaders();

    // ContentType
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    Resource file = new ByteArrayResource(new byte[] {123});

    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

    // Common form parameters.
    multipartBodyBuilder.part("text", "spring cloud");

    // Custom ContentType.
    multipartBodyBuilder.part(
        "json",
        """
                [{"name": "open feign"}, {"name": "gateway"}]""",
        MediaType.APPLICATION_JSON);

    multipartBodyBuilder.part("file", file, MediaType.TEXT_PLAIN);

    MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

    HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity =
        new HttpEntity<>(multipartBody, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response =
        restTemplate.postForEntity(getUploadUri(), httpEntity, String.class);

    Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
  }
}
