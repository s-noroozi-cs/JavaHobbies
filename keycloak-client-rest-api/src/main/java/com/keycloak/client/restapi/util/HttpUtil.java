package com.keycloak.client.restapi.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class HttpUtil {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static HttpResponse<String> submitForm(String url, Map<String, String> formData) throws Exception {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        String formDataBody = formBodyBuilder.toString();
        return sendPostRequest(url, Map.of("Content-Type", "application/x-www-form-urlencoded"), formDataBody);
    }

    public static HttpResponse<String> sendPostRequest(String url, Map<String, String> headerMap, String body) throws Exception {
        StringBuilder logMsg = new StringBuilder();
        try {
            String[] headers = headerMap.entrySet().stream()
                    .flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
                    .toArray(String[]::new);


            logMsg.append("""
                        --- Request ---
                        url: %s
                        method: POST
                        headers: %s
                        body: %s
                        
                    """.formatted(url,Arrays.toString(headers), body));


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers(headers)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = sendRequest(request);
            logMsg.append("""
                                        
                    --- Response ---
                    status code: %d
                    body: %s
                    """.formatted(response.statusCode(), response.body()));
            log.info(logMsg.toString());
            return response;
        } catch (Throwable ex) {
            logMsg.append("""
                                        
                    --- Exception ---
                    message: %s
                    """.formatted(ex.getMessage()));
            log.error(logMsg.toString());
            throw ex;
        }
    }

    private static HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
