package com.keycloak.client.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String username;

    @JsonIgnore
    private String userId;

    private boolean enabled;
    private List<String> realmRoles;
    private Map<String,List<String>> clientRoles;
}
