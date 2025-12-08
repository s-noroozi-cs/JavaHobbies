package com.hobi.webauthn.yubico.service;

import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import com.yubico.webauthn.extension.appid.AppId;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebAuthnService {

  private final RelyingParty relyingParty;
  private final Map<String, PublicKeyCredentialCreationOptions> registrationOptions =
      new ConcurrentHashMap<>();
  private final Map<String, PublicKeyCredentialRequestOptions> assertionOptions =
      new ConcurrentHashMap<>();
  private final Map<String, UserIdentity> userStorage = new ConcurrentHashMap<>();
  private final Map<String, Set<RegisteredCredential>> credentialStorage =
      new ConcurrentHashMap<>();

  public WebAuthnService() {
    this.relyingParty =
        RelyingParty.builder()
            .identity(generateIdentity())
            .credentialRepository(
                new CredentialRepository() {
                  @Override
                  public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(
                      String username) {
                    return getStoredCredentialIds(username);
                  }

                  @Override
                  public Optional<ByteArray> getUserHandleForUsername(String username) {
                    return Optional.of(toByteArray(username));
                  }

                  @Override
                  public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
                    return Optional.of(userHandle.getBase64Url());
                  }

                  @Override
                  public Optional<RegisteredCredential> lookup(
                      ByteArray credentialId, ByteArray userHandle) {
                    return lookupCredential(credentialId, userHandle);
                  }

                  @Override
                  public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
                    return lookupAllCredentials(credentialId);
                  }
                })
            .origins(new HashSet<>(Arrays.asList("http://localhost:8080")))
            .appId(makeAppId())
            .build();
  }

  private static Optional<AppId> makeAppId() {
    try {
      return Optional.ofNullable(new AppId("https://localhost:8080"));
    } catch (Throwable ex) {
      ex.printStackTrace();
      return Optional.empty();
    }
  }

  private ByteArray toByteArray(String data) {
    return new ByteArray(data.getBytes(StandardCharsets.UTF_8));
  }

  private RelyingPartyIdentity generateIdentity() {
    return RelyingPartyIdentity.builder().id("localhost").name("WebAuthn Demo").build();
  }

  // Registration Start
  public PublicKeyCredentialCreationOptions startRegistration(String username, String displayName) {
    UserIdentity userIdentity =
        UserIdentity.builder()
            .name(username)
            .displayName(displayName)
            .id(toByteArray(username))
            .build();

    PublicKeyCredentialCreationOptions options =
        relyingParty.startRegistration(
            StartRegistrationOptions.builder().user(userIdentity).build());

    registrationOptions.put(username, options);
    userStorage.put(username, userIdentity);

    return options;
  }

  // Registration Finish
  public boolean finishRegistration(String username, String responseJson)
      throws RegistrationFailedException, IOException {
    PublicKeyCredentialCreationOptions options = registrationOptions.get(username);
    if (options == null) {
      throw new RuntimeException("Registration session expired");
    }

    RegistrationResult result =
        relyingParty.finishRegistration(
            FinishRegistrationOptions.builder()
                .request(options)
                .response(PublicKeyCredential.parseRegistrationResponseJson(responseJson))
                .build());

    // Store the credential
    RegisteredCredential credential =
        RegisteredCredential.builder()
            .credentialId(result.getKeyId().getId())
            .userHandle(toByteArray(username))
            .publicKeyCose(result.getPublicKeyCose())
            .signatureCount(result.getSignatureCount())
            .build();

    credentialStorage.computeIfAbsent(username, k -> new HashSet<>()).add(credential);

    registrationOptions.remove(username);
    return true;
  }

  // Authentication Start
  public PublicKeyCredentialRequestOptions startAuthentication(String username) {
    PublicKeyCredentialRequestOptions options =
        relyingParty
            .startAssertion(StartAssertionOptions.builder().username(Optional.of(username)).build())
            .getPublicKeyCredentialRequestOptions();

    assertionOptions.put(username, options);
    return options;
  }

  // Authentication Finish
  public boolean finishAuthentication(String username, String responseJson)
      throws AssertionFailedException, IOException {
    PublicKeyCredentialRequestOptions options = assertionOptions.get(username);
    if (options == null) {
      throw new RuntimeException("Authentication session expired");
    }

    var request =
        AssertionRequest.builder()
            .publicKeyCredentialRequestOptions(options)
            .username(username)
            .userHandle(toByteArray(username))
            .build();
    AssertionResult result =
        relyingParty.finishAssertion(
            FinishAssertionOptions.builder()
                .request(request)
                .response(PublicKeyCredential.parseAssertionResponseJson(responseJson))
                .build());

    assertionOptions.remove(username);
    return result.isSuccess();
  }

  // Helper methods for credential storage
  private Set<PublicKeyCredentialDescriptor> getStoredCredentialIds(String username) {
    Set<RegisteredCredential> creds = credentialStorage.get(username);
    if (creds == null) return new HashSet<>();

    Set<PublicKeyCredentialDescriptor> descriptors = new HashSet<>();
    for (RegisteredCredential cred : creds) {
      descriptors.add(
          PublicKeyCredentialDescriptor.builder()
              .id(cred.getCredentialId())
              .type(PublicKeyCredentialType.PUBLIC_KEY)
              .build());
    }
    return descriptors;
  }

  private Optional<RegisteredCredential> lookupCredential(
      ByteArray credentialId, ByteArray userHandle) {
    String username = userHandle.getBase64Url();
    Set<RegisteredCredential> creds = credentialStorage.get(username);
    if (creds != null) {
      return creds.stream().filter(c -> c.getCredentialId().equals(credentialId)).findFirst();
    }
    return Optional.empty();
  }

  private Set<RegisteredCredential> lookupAllCredentials(ByteArray credentialId) {
    Set<RegisteredCredential> allMatches = new HashSet<>();
    for (Set<RegisteredCredential> creds : credentialStorage.values()) {
      for (RegisteredCredential cred : creds) {
        if (cred.getCredentialId().equals(credentialId)) {
          allMatches.add(cred);
        }
      }
    }
    return allMatches;
  }
}
