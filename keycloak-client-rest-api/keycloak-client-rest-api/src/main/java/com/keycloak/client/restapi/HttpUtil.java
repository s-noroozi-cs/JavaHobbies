package com.keycloak.client.restapi;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class HttpUtil {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static HttpResponse<String> submitForm(String url, Map<String, String> formData) throws Exception {
        StringBuilder logMsg = new StringBuilder();
        try {
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

            logMsg.append("""
                        --- Request ---
                        url: %s
                        method: POST
                        body: %s
                    """.formatted(url, formDataBody));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type","application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formDataBody))
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
