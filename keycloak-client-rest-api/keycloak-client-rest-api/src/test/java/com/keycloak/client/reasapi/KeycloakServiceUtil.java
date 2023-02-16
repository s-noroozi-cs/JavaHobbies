package com.keycloak.client.reasapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.keycloak.client.restapi.KeycloakAdminRestApi;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.net.http.HttpResponse;

public class KeycloakServiceUtil {
    private KeycloakAdminRestApi service = new KeycloakAdminRestApi();

    @Test
    @SneakyThrows
    void get_admin_token (){
        HttpResponse<String> response = service.getAdminToken();
        Assertions.assertEquals(200,response.statusCode());
        JsonObject js = JsonParser.parseReader(new StringReader(response.body()))
                .getAsJsonObject();
        Assertions.assertTrue(js.has("access_token"));
    }
}
