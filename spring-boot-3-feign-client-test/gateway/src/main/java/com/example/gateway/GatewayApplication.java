package com.example.gateway;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class GatewayApplication {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping(value = "/v3/api-docs/swagger-config", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> swaggerConfig() {
        JsonObject js = new JsonObject();
        js.addProperty("configUrl", "/v3/api-docs/swagger-config");

        JsonArray jsUrls = new JsonArray();
        for (String name : discoveryClient.getServices()) {
            if (name.equals(applicationName) || name.equals("consul")) {
                //ignore
            } else {
                JsonObject url = new JsonObject();
                url.addProperty("url", "/%s/v3/api-docs".formatted(name));
                url.addProperty("name", name);

                jsUrls.add(url);
            }
        }

        js.add("urls", jsUrls);
        return ResponseEntity.ok(js.toString());
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
