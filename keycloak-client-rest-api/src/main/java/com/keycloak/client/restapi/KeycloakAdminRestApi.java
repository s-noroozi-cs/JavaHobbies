package com.keycloak.client.restapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.keycloak.client.restapi.config.JacksonConfig;
import com.keycloak.client.restapi.config.KeycloakConfig;
import com.keycloak.client.restapi.model.KeycloakClientModel;
import com.keycloak.client.restapi.model.KeycloakRoleModel;
import com.keycloak.client.restapi.model.KeycloakTokenModel;
import com.keycloak.client.restapi.model.UserModel;
import com.keycloak.client.restapi.util.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.http.HttpResponse;
import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakAdminRestApi {
    private KeycloakConfig config = new KeycloakConfig();
    private final String version = "20.0.3";
    private final String docRef = "https://www.keycloak.org/docs-api/20.0.3/rest-api/index.html";
    private final String adminRestApiUrlSchema = "/admin/realms";

    private List<KeycloakRoleModel> realmRoles;
    private List<KeycloakClientModel> clients;

    private void getClientRole(KeycloakClientModel model) {
        try {
            String adminToken = getAdminToken().orElseThrow().getAccessToken();

            String url = config.getServerUrl()
                    + adminRestApiUrlSchema
                    + "/%s/clients/%s/roles"
                    .formatted(config.getAppRealm(), model.getId());
            HttpResponse<String> response = HttpUtil.sendGetRequest(url,
                    Map.of("Authorization", "Bearer " + adminToken));
            TypeReference<List<KeycloakRoleModel>> typeRef = new TypeReference<>() {
            };
            model.setRoles(JacksonConfig.getMapper().readValue(response.body(), typeRef));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private String getClientRoleId(String clientName, String roleName) throws Exception {
        if (clients == null) {
            synchronized (this) {
                //double check
                if (clients == null) {
                    String adminToken = getAdminToken().orElseThrow().getAccessToken();

                    String url = config.getServerUrl()
                            + adminRestApiUrlSchema
                            + "/%s/clients"
                            .formatted(config.getAppRealm());
                    HttpResponse<String> response = HttpUtil.sendGetRequest(url,
                            Map.of("Authorization", "Bearer " + adminToken));
                    TypeReference<List<KeycloakClientModel>> typeRef = new TypeReference<>() {
                    };
                    clients = JacksonConfig.getMapper().readValue(response.body(), typeRef);
                    clients.stream().forEach(this::getClientRole);
                }
            }
        }
        return clients.stream()
                .filter(i -> i.getClientId().equals(clientName))
                .flatMap(i -> i.getRoles().stream())
                .filter(i -> i.getName().equals(roleName))
                .map(KeycloakRoleModel::getId)
                .findFirst().orElseThrow();
    }

    private String getRealmRoleId(String roleName) throws Exception {
        if (realmRoles == null) {
            synchronized (this) {
                //double check
                if (realmRoles == null) {
                    String adminToken = getAdminToken().orElseThrow().getAccessToken();

                    String url = config.getServerUrl()
                            + adminRestApiUrlSchema
                            + "/%s/roles"
                            .formatted(config.getAppRealm());
                    HttpResponse<String> response = HttpUtil.sendGetRequest(url,
                            Map.of("Authorization", "Bearer " + adminToken));
                    TypeReference<List<KeycloakRoleModel>> typeRef = new TypeReference<>() {
                    };
                    realmRoles = JacksonConfig.getMapper().readValue(response.body(), typeRef);
                }
            }
        }
        return realmRoles.stream()
                .filter(i -> i.getName().equals(roleName))
                .map(KeycloakRoleModel::getId)
                .findFirst().orElseThrow();
    }

    public Optional<KeycloakTokenModel> getAdminToken() throws Exception {
        String url = config.getServerUrl() + ("/realms/%s/protocol/openid-connect/token")
                .formatted(config.getAdminRealm());
        Map<String, String> params = Map.of(
                "grant_type", "password"
                , "client_id", config.getAdminClientId()
                , "username", config.getAdminUser()
                , "password", config.getAdminPass()
        );
        HttpResponse<String> response = HttpUtil.submitForm(url, params);
        if (response.statusCode() != 200)
            return Optional.empty();
        else {
            return Optional.of(JacksonConfig.getMapper()
                    .readValue(response.body(), KeycloakTokenModel.class));
        }
    }

    public Optional<UserModel> createUser(UserModel userModel) throws Exception {
        String adminToken = getAdminToken().orElseThrow().getAccessToken();

        String url = config.getServerUrl() + adminRestApiUrlSchema + "/%s/users".formatted(config.getAppRealm());

        String userModelJson = JacksonConfig.getMapper().writeValueAsString(userModel);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", "Bearer " + adminToken);

        HttpResponse<String> response = HttpUtil.sendPostRequest(url, headerMap, userModelJson);
        if (response.statusCode() != 201) {
            return Optional.empty();
        } else {
            String locationHeader = response.headers().firstValue("location").orElseThrow();
            userModel.setUserId(locationHeader.substring(locationHeader.lastIndexOf("/") + 1));

            addRealmRole(userModel);
            addClientRole(userModel);

            return Optional.of(userModel);
        }
    }

    public void addRealmRole(UserModel userModel) throws Exception {
        if (userModel.getRealmRoles() == null || userModel.getRealmRoles().size() == 0)
            return;

        String adminToken = getAdminToken().orElseThrow().getAccessToken();

        String url = config.getServerUrl()
                + adminRestApiUrlSchema
                + "/%s/users/%s/role-mappings/realm"
                .formatted(config.getAppRealm(), userModel.getUserId());
        JsonArray body = new JsonArray();
        for (String roleName : userModel.getRealmRoles()) {
            JsonObject js = new JsonObject();
            js.addProperty("id", getRealmRoleId(roleName));
            js.addProperty("name", roleName);
            body.add(js);
        }
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", "Bearer " + adminToken);
        HttpResponse<String> response = HttpUtil.sendPostRequest(url, headerMap, body.toString());
        if (response.statusCode() != 204)
            throw new RuntimeException(response.body());
    }

    public void addClientRole(UserModel userModel) throws Exception {
        if (userModel.getClientRoles() == null || userModel.getClientRoles().size() == 0)
            return;

        String adminToken = getAdminToken().orElseThrow().getAccessToken();

        for (String clientId : userModel.getClientRoles().keySet()) {
            JsonArray body = new JsonArray();

            for (String roleName : userModel.getClientRoles().get(clientId)) {
                JsonObject js = new JsonObject();
                js.addProperty("id", getClientRoleId(clientId, roleName));
                js.addProperty("name", roleName);
                body.add(js);
            }

            String id = clients.stream()
                    .filter(i -> i.getClientId().equals(clientId))
                    .map(KeycloakClientModel::getId)
                    .findFirst().orElseThrow();

            String url = config.getServerUrl()
                    + adminRestApiUrlSchema
                    + "/%s/users/%s/role-mappings/clients/%s"
                    .formatted(config.getAppRealm(), userModel.getUserId(), id);

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Type", "application/json");
            headerMap.put("Authorization", "Bearer " + adminToken);

            HttpResponse<String> response = HttpUtil.sendPostRequest(url, headerMap, body.toString());
            if (response.statusCode() != 204)
                throw new RuntimeException(response.body());
        }
    }

    public HttpResponse<String> removeUser(String userId) throws Exception {
        String adminToken = getAdminToken().orElseThrow().getAccessToken();

        String url = config.getServerUrl() + adminRestApiUrlSchema + "/%s/users/%s"
                .formatted(config.getAppRealm(), userId);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + adminToken);

        return HttpUtil.sendDeleteRequest(url, headerMap);
    }
}
