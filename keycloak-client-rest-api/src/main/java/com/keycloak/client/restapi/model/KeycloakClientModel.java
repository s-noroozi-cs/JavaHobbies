package com.keycloak.client.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakClientModel {
    private String id;
    private String clientId;
    private List<KeycloakRoleModel> roles;
}
