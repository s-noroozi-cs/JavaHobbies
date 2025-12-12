package com.hobi.webauthn.yubico.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.stream.Collectors;

public class RegisterServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    // Generate challenge (should be random and at least 16 bytes)
    byte[] challenge = new byte[32];
    new SecureRandom().nextBytes(challenge);

    // Store challenge in session for later verification
    HttpSession session = req.getSession();
    session.setAttribute("webauthn_challenge", challenge);

    // Create PublicKeyCredentialCreationOptions
    String options = createRegistrationOptions(challenge);

    resp.setContentType("application/json");
    resp.getWriter().write(options);
  }

  private String createRegistrationOptions(byte[] challenge) {
    // Simple JSON creation (you might want to use proper JSON handling)
    String userVerification = "preferred";
    String attachment = "cross-platform"; // "platform"
    String attestation = "direct";
    String requireResidentKey = "true";
    String residentKey = "required";
    return String.format(
        "{\"publicKey\":{"
            + "\"rp\":{\"name\":\"My Website\",\"id\":\"%s\"},"
            + "\"user\":{"
            + "\"id\":\"%s\","
            + "\"name\":\"%s\","
            + "\"displayName\":\"%s\""
            + "},"
            + "\"challenge\":\"%s\","
            + "\"pubKeyCredParams\":["
            + "{\"type\":\"public-key\",\"alg\":-8},"
            + // ED25519
            "{\"type\":\"public-key\",\"alg\":-7},"
            + // ES256
            "{\"type\":\"public-key\",\"alg\":-257}"
            + // RS256
            "],"
            + "\"timeout\":60000,"
            + "\"attestation\":\""
            + attestation
            + "\","
            + "\"authenticatorSelection\":{"
            /*+ "\"authenticatorAttachment\":\""
            + attachment
            + "\","*/
            + "\"residentKey\":\""
            + residentKey
            + "\""
            + ",\"requireResidentKey\":"
            + requireResidentKey
            + ","
            + "\"userVerification\":\""
            + userVerification
            + "\""
            + "}"
            + "}}",
        URLEncoder.encode("localhost", StandardCharsets.UTF_8),
        Base64.getUrlEncoder().encodeToString("user123".getBytes()),
        "user@example.com",
        "User Display Name",
        Base64.getUrlEncoder().encodeToString(challenge));
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    // Parse the credential creation response
    String json = req.getReader().lines().collect(Collectors.joining());

    // Basic validation - in reality, parse JSON properly
    HttpSession session = req.getSession();
    byte[] expectedChallenge = (byte[]) session.getAttribute("webauthn_challenge");

    if (expectedChallenge == null) {
      resp.sendError(400, "No challenge found");
      return;
    }

    // Here you would:
    // 1. Parse and validate the response
    // 2. Verify the attestation
    // 3. Store the public key in your database

    session.removeAttribute("webauthn_challenge");
    resp.setStatus(200);
  }
}
