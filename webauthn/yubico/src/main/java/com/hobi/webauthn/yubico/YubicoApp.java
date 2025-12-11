package com.hobi.webauthn.yubico;

import com.yubico.webauthn.data.AttestationObject;
import com.yubico.webauthn.data.AuthenticatorData;
import com.yubico.webauthn.data.ByteArray;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class YubicoApp {
  public static void main(String[] args) {
    // SpringApplication.run(YubicoApp.class, args);
      try {
          // Convert to ByteArray
          ByteArray attestationObjectBytes = ByteArray.fromBase64Url("o2NmbXRkbm9uZWdhdHRTdG10oGhhdXRoRGF0YViUSZYN5YgOjGh0NBcPZHZgW4_krrmihjLHmVzzuoMdl2NdAAAAAOqbjWZNAR0hPOS2tIy1ddQAELrWJnVaNvRlfMCiumlcT8qlAQIDJiABIVggd-pnn5RrJtC9Tqtc3xop7iVQf7ZNuVfKZUPkJi7xKKYiWCAZ0yDnVRG_4AQ-J6Ihc5_JLfn8fg4v2wdy40sNPEA4ng");

          // Parse attestation object
          AttestationObject attestationObject =
                  new AttestationObject(attestationObjectBytes);

          // Get authenticator data
          AuthenticatorData authData = attestationObject.getAuthenticatorData();

          // Get attestation statement format
          String format = attestationObject.getFormat();

          // Get attestation statement
//          AttestationStatement attestationStatement =
//                  attestationObject.getAttestationStatement();

          System.out.println("Format: " + format);
          System.out.println("RP ID Hash: " + authData.getRpIdHash().getBase64Url());
          System.out.println("Sign Count: " + authData.getSignatureCounter());

      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
