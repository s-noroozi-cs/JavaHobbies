package com.keycloak.client.restapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonConfig {
    private static final ObjectMapper MAPPER = init();

    private static ObjectMapper init(){
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    public static ObjectMapper getMapper(){
        return MAPPER;
    }
}
