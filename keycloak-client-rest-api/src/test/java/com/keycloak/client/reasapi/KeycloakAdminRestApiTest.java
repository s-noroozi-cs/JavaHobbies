package com.keycloak.client.reasapi;

import com.keycloak.client.restapi.KeycloakAdminRestApi;
import com.keycloak.client.restapi.model.KeycloakTokenModel;
import com.keycloak.client.restapi.model.UserModel;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

public class KeycloakAdminRestApiTest {
    private static KeycloakAdminRestApi service = new KeycloakAdminRestApi();
    private static List<String> createdUserIds = new LinkedList<>();

    @Test
    @SneakyThrows
    void get_admin_token() {
        Optional<KeycloakTokenModel> response = service.getAdminToken();
        Assertions.assertTrue(
                response.map(KeycloakTokenModel::getAccessToken)
                        .map(i -> !i.isBlank())
                        .orElse(false));
    }

    @Test
    @SneakyThrows
    void create_user() {
        String username = UUID.randomUUID().toString();
        UserModel model = new UserModel();
        model.setUsername(username);
        model.setRealmRoles(List.of("realm_role_a"));
        model.setClientRoles(Map.of("client_b",
                List.of("client_role_b1", "client_role_b2")));
        model.setEnabled(true);
        Optional<UserModel> response = service.createUser(model);
        Assertions.assertTrue(
                response.map(UserModel::getUserId)
                        .map(i -> !i.isBlank())
                        .orElse(false));
        createdUserIds.add(response.get().getUserId());
    }

    @AfterAll
    static void remove_created_users() {
        Function<String, Boolean> sendRequest = id -> {
            try {
                return service.removeUser(id).statusCode() == 204;
            } catch (Throwable ex) {
                return false;
            }
        };
        Assertions.assertTrue(createdUserIds.stream()
                .map(sendRequest)
                .allMatch(Boolean::booleanValue));
    }


}
