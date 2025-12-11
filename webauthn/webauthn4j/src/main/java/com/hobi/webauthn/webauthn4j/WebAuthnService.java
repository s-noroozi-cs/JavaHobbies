package com.hobi.webauthn.webauthn4j;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.*;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.util.Base64UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

@Service
public class WebAuthnService {

  // In-memory storage for demo (use Redis or database in production)
  private final Map<String, byte[]> challengeStore = new HashMap<>();
  private final Map<String, UserData> userStore = new HashMap<>();

  private final WebAuthnManager webAuthnManager =
      WebAuthnManager.createNonStrictWebAuthnManager(new ObjectConverter());

  // Generate registration options
  public Map<String, Object> generateRegistrationOptions(String username) {
    try {
      // Generate random challenge
      byte[] challenge = new byte[32];
      new SecureRandom().nextBytes(challenge);

      // Create user ID from username
      byte[] userId = username.getBytes(StandardCharsets.UTF_8);

      // Store challenge with random ID
      String challengeId = UUID.randomUUID().toString();
      challengeStore.put(challengeId, challenge);

      // Create response
      Map<String, Object> options = new HashMap<>();
      options.put("challenge", Base64UrlUtil.encodeToString(challenge));
      options.put(
          "rp",
          Map.of(
              "name", "WebAuthn Demo",
              "id", "localhost"));
      options.put(
          "user",
          Map.of(
              "id", Base64UrlUtil.encodeToString(userId),
              "name", username,
              "displayName", username));

      // Supported public key algorithms
      List<Map<String, Object>> pubKeyCredParams =
          Arrays.asList(
              Map.of("type", "public-key", "alg", -7), // ES256
              Map.of("type", "public-key", "alg", -257) // RS256
              );
      options.put("pubKeyCredParams", pubKeyCredParams);

      options.put("timeout", 60000);
      options.put("attestation", "direct");

      // Return both options and challenge ID
      Map<String, Object> response = new HashMap<>();
      response.put("options", options);
      response.put("challengeId", challengeId);

      return response;

    } catch (Exception e) {
      throw new RuntimeException("Failed to generate registration options", e);
    }
  }

  // Verify registration
  public boolean verifyRegistration(
      String challengeId,
      String clientDataJSON,
      String attestationObject,
      String credentialId,
      String username) {

    try {
      byte[] storedChallenge = challengeStore.remove(challengeId);
      if (storedChallenge == null) {
        throw new RuntimeException("Challenge not found or expired");
      }

      // Decode base64url strings
      byte[] clientDataJSONBytes = Base64UrlUtil.decode(clientDataJSON);
      byte[] attestationObjectBytes = Base64UrlUtil.decode(attestationObject);

      // Create registration data
      RegistrationRequest registrationRequest =
          new RegistrationRequest(attestationObjectBytes, clientDataJSONBytes);

      // Create server property
      ServerProperty serverProperty =
          new ServerProperty(
              new Origin("http://localhost:8080"), "localhost", storedChallenge, null);

      // Parse and validate
      RegistrationData registrationData = webAuthnManager.parse(registrationRequest);
      webAuthnManager.validate(registrationData, serverProperty);

      // Store user data
      UserData userData = new UserData();
      userData.username = username;
      userData.credentialId = credentialId;
      userData.publicKey =
          registrationData
              .getAttestationObject()
              .getAuthenticatorData()
              .getAttestedCredentialData()
              .getCOSEKey()
              .getBytes();
      userData.counter =
          registrationData.getAttestationObject().getAuthenticatorData().getSignCount();

      userStore.put(username, userData);

      return true;

    } catch (Exception e) {
      throw new RuntimeException("Registration verification failed", e);
    }
  }

  // Generate authentication options
  public Map<String, Object> generateAuthenticationOptions(String username) {
    try {
      // Generate random challenge
      byte[] challenge = new byte[32];
      new SecureRandom().nextBytes(challenge);

      // Store challenge
      String challengeId = UUID.randomUUID().toString();
      challengeStore.put(challengeId, challenge);

      // Get user's credential if exists
      UserData userData = userStore.get(username);
      List<Map<String, Object>> allowCredentials = new ArrayList<>();

      if (userData != null) {
        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "public-key");
        credential.put("id", userData.credentialId);
        credential.put("transports", Arrays.asList("usb", "nfc", "ble", "internal"));
        allowCredentials.add(credential);
      }

      // Create options
      Map<String, Object> options = new HashMap<>();
      options.put("challenge", Base64UrlUtil.encodeToString(challenge));
      options.put("rpId", "localhost");
      options.put("allowCredentials", allowCredentials);
      options.put("userVerification", "preferred");
      options.put("timeout", 60000);

      Map<String, Object> response = new HashMap<>();
      response.put("options", options);
      response.put("challengeId", challengeId);

      return response;

    } catch (Exception e) {
      throw new RuntimeException("Failed to generate authentication options", e);
    }
  }

  // Verify authentication
  public boolean verifyAuthentication(
      String challengeId,
      String credentialId,
      String clientDataJSON,
      String authenticatorData,
      String signature,
      String username) {

    try {
      byte[] storedChallenge = challengeStore.remove(challengeId);
      if (storedChallenge == null) {
        throw new RuntimeException("Challenge not found or expired");
      }

      UserData userData = userStore.get(username);
      if (userData == null) {
        throw new RuntimeException("User not found");
      }

      // Decode base64url strings
      byte[] credentialIdBytes = Base64UrlUtil.decode(credentialId);
      byte[] clientDataJSONBytes = Base64UrlUtil.decode(clientDataJSON);
      byte[] authenticatorDataBytes = Base64UrlUtil.decode(authenticatorData);
      byte[] signatureBytes = Base64UrlUtil.decode(signature);

      // Create authentication request
      AuthenticationRequest authenticationRequest =
          new AuthenticationRequest(
              credentialIdBytes, clientDataJSONBytes, authenticatorDataBytes, signatureBytes, null);

      // Create server property
      ServerProperty serverProperty =
          new ServerProperty(
              new Origin("http://localhost:8080"), "localhost", storedChallenge, null);

      // For simplicity, we skip authenticator validation in this minimal example
      // In production, you would need to store and validate the authenticator

      // Parse and validate
      AuthenticationData authenticationData = webAuthnManager.parse(authenticationRequest);
      webAuthnManager.verify()

      return true;

    } catch (Exception e) {
      throw new RuntimeException("Authentication verification failed", e);
    }
  }

  public static class UserData {
    public String username;
    public String credentialId;
    public byte[] publicKey;
    public long counter;
  }
}
