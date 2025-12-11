package com.hobi.webauthn.yubico.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.hobi.webauthn.yubico.service.WebAuthnService;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.PublicKeyCredentialRequestOptions;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebAuthnWebHandler {
  private final ObjectMapper objectMapper;
  private final WebAuthnService webAuthnService = new WebAuthnService();

  public WebAuthnWebHandler() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new Jdk8Module());
  }

  public void sendError(HttpServletResponse response, String message, int status)
      throws IOException {
    response.setStatus(status);
    objectMapper.writeValue(response.getWriter(), new ErrorResponse(message, status));
  }

  public void handleRegistrationStart(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    // Parse request body
    Map<String, String> body =
        objectMapper.readValue(
            request.getInputStream(), new TypeReference<Map<String, String>>() {});

    String username = body.get("username");
    String displayName = body.get("displayName");

    if (username == null || displayName == null) {
      sendError(response, "Username and displayName required", 400);
      return;
    }

    // Start registration
    PublicKeyCredentialCreationOptions options =
        webAuthnService.startRegistration(username, displayName);

    // Return options to client
    objectMapper.writeValue(response.getWriter(), options);
  }

  public void handleRegistrationFinish(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    Map<String, String> body =
        objectMapper.readValue(
            request.getInputStream(), new TypeReference<Map<String, String>>() {});

    String username = body.get("username");
    String credentialResponse = body.get("credential");

    if (username == null || credentialResponse == null) {
      sendError(response, "Username and credential response required", 400);
      return;
    }

    try {
      boolean success = webAuthnService.finishRegistration(username, credentialResponse);

      Map<String, Object> result = new HashMap<>();
      result.put("success", success);
      result.put("message", success ? "Registration successful" : "Registration failed");

      objectMapper.writeValue(response.getWriter(), result);
    } catch (RegistrationFailedException e) {
      sendError(response, "Registration failed: " + e.getMessage(), 400);
    }
  }

  public void handleLoginStart(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    Map<String, String> body =
        objectMapper.readValue(
            request.getInputStream(), new TypeReference<Map<String, String>>() {});

    String username = body.get("username");

    if (username == null) {
      sendError(response, "Username required", 400);
      return;
    }

    PublicKeyCredentialRequestOptions options = webAuthnService.startAuthentication(username);

    objectMapper.writeValue(response.getWriter(), options);
  }

  public void handleLoginFinish(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    Map<String, String> body =
        objectMapper.readValue(
            request.getInputStream(), new TypeReference<Map<String, String>>() {});

    String username = body.get("username");
    String assertionResponse = body.get("assertion");

    if (username == null || assertionResponse == null) {
      sendError(response, "Username and assertion response required", 400);
      return;
    }

    try {
      boolean success = webAuthnService.finishAuthentication(username, assertionResponse);

      Map<String, Object> result = new HashMap<>();
      result.put("success", success);
      result.put("message", success ? "Authentication successful" : "Authentication failed");

      // Set session on successful authentication
      if (success) {
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        session.setAttribute("authenticated", true);
        result.put("sessionId", session.getId());
      }

      objectMapper.writeValue(response.getWriter(), result);
    } catch (AssertionFailedException e) {
      sendError(response, "Authentication failed: " + e.getMessage(), 400);
    }
  }

  // Helper class for JSON type reference
  private static class TypeReference<T> extends com.fasterxml.jackson.core.type.TypeReference<T> {}
}
