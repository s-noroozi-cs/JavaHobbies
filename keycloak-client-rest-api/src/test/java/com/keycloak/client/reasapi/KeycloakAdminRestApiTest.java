package com.keycloak.client.reasapi;

import com.keycloak.client.restapi.KeycloakAdminRestApi;
import com.keycloak.client.restapi.model.KeycloakTokenModel;
import com.keycloak.client.restapi.model.UserModel;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.*;

public class KeycloakAdminRestApiTest {
    private KeycloakAdminRestApi service = new KeycloakAdminRestApi();
    private List<String> createdUserIds = new LinkedList<>();

    @Test
    @SneakyThrows
    void get_admin_token() {
        Optional<KeycloakTokenModel> response = service.getAdminToken();
        Assertions.assertTrue(response.isPresent());
        Assertions.assertNotNull(response.get().getAccessToken());
    }

    @Test
    @SneakyThrows
    void create_user() {
        String username = UUID.randomUUID().toString();
        UserModel model = new UserModel(username);
        HttpResponse<String> response = service.createUser(model);
        Assertions.assertEquals(201, response.statusCode());
    }


}
