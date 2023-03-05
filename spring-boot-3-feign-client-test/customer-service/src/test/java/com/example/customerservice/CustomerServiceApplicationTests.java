package com.example.customerservice;

import com.example.customerservice.client.MerchantClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration
@ActiveProfiles("test")
class CustomerServiceApplicationTests {

    @MockBean
    private MerchantClient merchantClient;


    @LocalServerPort
    private int port;

    @SneakyThrows
    @Test
    void test_merchant_mock() {


        final String mockResponse = UUID.randomUUID().toString();

        Mockito
                .when(merchantClient.get(Mockito.any()))
                .thenReturn(ResponseEntity.ok(mockResponse));


        URI uri = new URI("http://localhost:" + port +
                "/api/v1/customers/test/merchants/" + System.currentTimeMillis());
        HttpRequest request = HttpRequest
                .newBuilder(uri)
                .GET()
                .build();
        HttpResponse response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assertions.assertEquals(mockResponse,response.body());

    }
}
