package com.keycloak.client.restapi.config;

import com.keycloak.client.restapi.util.ConfigUtil;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KeycloakConfig {
    private final String serverUrl = ConfigUtil.getConfig("server.url","http://localhost:8080");

    private final String adminRealm = ConfigUtil.getConfig("admin.realm","master");
    private final String adminUser = ConfigUtil.getConfig("admin.user","admin");
    private final String adminPass = ConfigUtil.getConfig("admin.pass","admin");
    private final String adminClientId = ConfigUtil.getConfig("admin.client.id","admin-cli");

    private final String appRealm = ConfigUtil.getConfig("app.realm","my_test_realm");

}
