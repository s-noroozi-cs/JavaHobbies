package com.keycloak.client.restapi.util;

import java.util.Optional;

public class ConfigUtil {
    public static String getConfig(String name, String defaultValue) {
        return Optional.ofNullable(System.getProperty(name))
                .or(() -> Optional.ofNullable(System.getenv(name)))
                .orElseGet(() -> defaultValue);
    }
}
