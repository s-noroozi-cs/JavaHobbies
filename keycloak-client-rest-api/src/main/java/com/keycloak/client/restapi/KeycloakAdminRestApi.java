package com.keycloak.client.restapi;

import com.google.gson.JsonObject;
import com.keycloak.client.restapi.config.JacksonConfig;
import com.keycloak.client.restapi.config.KeycloakConfig;
import com.keycloak.client.restapi.model.KeycloakTokenModel;
import com.keycloak.client.restapi.model.UserModel;
import com.keycloak.client.restapi.util.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakAdminRestApi {
    private KeycloakConfig config = new KeycloakConfig();
    private final String version = "20.0.3";
    private final String docRef = "https://www.keycloak.org/docs-api/20.0.3/rest-api/index.html";
    private final String adminRestApiUrlSchema = "/admin/realms";

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

    public HttpResponse<String> createUser(UserModel userModel) throws Exception {
        String adminToken = getAdminToken().orElseThrow().getAccessToken();

        String url = config.getServerUrl() + adminRestApiUrlSchema + "/%s/users".formatted(config.getAppRealm());

        JsonObject js = new JsonObject();
        js.addProperty("username", userModel.getUsername());

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", "Bearer " + adminToken);

        return HttpUtil.sendPostRequest(url, headerMap, js.toString());
    }
}
