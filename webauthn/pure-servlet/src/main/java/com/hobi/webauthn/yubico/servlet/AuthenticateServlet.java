package com.hobi.webauthn.yubico.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.stream.Collectors;

public class AuthenticateServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    // Generate challenge
    byte[] challenge = new byte[32];
    new SecureRandom().nextBytes(challenge);

    // Store in session
    HttpSession session = req.getSession();
    session.setAttribute("auth_challenge", challenge);

    // Get user's credential ID from your database
    String credentialId = "user-credential-id"; // Fetch from DB

    String options =
        String.format(
            "{\"publicKey\":{"
                + "\"challenge\":\"%s\","
                + "\"timeout\":60000,"
                + "\"rpId\":\"%s\","
                /*+ "\"allowCredentials\":["
                + "{\"type\":\"public-key\",\"id\":\"%s\"}"
                + "],"*/
                + "\"userVerification\":\"preferred\""
                + "}}",
            Base64.getUrlEncoder().encodeToString(challenge), "localhost", credentialId);

    resp.setContentType("application/json");
    resp.getWriter().write(options);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    String json = req.getReader().lines().collect(Collectors.joining());

    HttpSession session = req.getSession();
    byte[] expectedChallenge = (byte[]) session.getAttribute("auth_challenge");

    if (expectedChallenge == null) {
      resp.sendError(400, "No challenge found");
      return;
    }

    // Here you would:
    // 1. Parse and validate the assertion
    // 2. Verify the signature using stored public key
    // 3. Check the challenge matches

    session.removeAttribute("auth_challenge");

    // Authentication successful
    session.setAttribute("authenticated", true);
    resp.setStatus(200);
  }
}