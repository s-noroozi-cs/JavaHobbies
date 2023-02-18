package com.keycloak.client.restapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.http.HttpResponse;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakAdminRestApi {
    private KeycloakConfig config = new KeycloakConfig();
    private final String version = "20.0.3";
    private final String docRef = "https://www.keycloak.org/docs-api/20.0.3/rest-api/index.html";


    public HttpResponse<String> getAdminToken() throws Exception {
        String url = config.getServerUrl() + ("/realms/%s/protocol/openid-connect/token")
                .formatted(config.getAdminRealm());
        Map<String, String> params = Map.of(
                "grant_type", "password"
                , "client_id", config.getAdminClientId()
                , "username", config.getAdminUser()
                , "password", config.getAdminPass()
        );
        return HttpUtil.submitForm(url, params);
    }

    public HttpResponse<String> createUser(UserModel userModel){
        String url = config.getServerUrl() + ("/realms/%s/protocol/openid-connect/token")
                .formatted(config.getAdminRealm());
    }
}
