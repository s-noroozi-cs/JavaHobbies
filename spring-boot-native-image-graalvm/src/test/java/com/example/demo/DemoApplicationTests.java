//package com.example.demo;
//
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
//    @LocalServerPort
//    private int port;
//
//    @Test
//    void contextLoads() {
//    }
//
//
//    private URI makeRequestPath(String path) throws Exception {
//        return new URI("http://localhost:" + port + path);
//    }
//
//
//    private HttpResponse<String> sendGetRequest(String url) throws Exception {
//        HttpRequest request = HttpRequest
//                .newBuilder(makeRequestPath(url))
//                .GET()
//                .build();
//        return HttpClient
//                .newBuilder()
//                .build()
//                .send(request, HttpResponse.BodyHandlers.ofString());
//    }
//
//    @Test
//    void fetch_actuator_health_check() throws Exception {
//        HttpResponse<String> response = sendGetRequest("/actuator/health");
//        Assertions.assertEquals(200, response.statusCode());
//        JSONObject js = new JSONObject(response.body());
//        String openApiVersion = js.getString("openapi");
//        Assertions.assertEquals("3.0.1", openApiVersion);
//    }
//
//}
