import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.RegistrationData;
import com.webauthn4j.data.RegistrationParameters;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegistrationFlowTests {
  private static final String REG_REQ = "register-response.json";
  private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  private static final WebAuthnManager webAuthnManager =
      WebAuthnManager.createNonStrictWebAuthnManager();

  private String fetch_content(String resource) {
    try (var stream = classLoader.getResourceAsStream(resource)) {
      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (Throwable ex) {
      throw new RuntimeException(ex.getMessage(), ex);
    }
  }

  RegistrationData get_registration_data() {
    return webAuthnManager.parseRegistrationResponseJSON(fetch_content(REG_REQ));
  }

  @Test
  public void parse_reg_response() {
    Assertions.assertNotNull(get_registration_data());
  }

  @Test
  void verify_reg_response() {

    // Server properties
    Origin origin = Origin.create("http://localhost:8080") /* set origin */;
    String rpId = "localhost" /* set rpId */;
    Challenge challenge =
        new DefaultChallenge("7LzBmPxNlDJXl97MDiQWIyxCWiAm2phVKPDZQ9dCiwU") /* set challenge */;
    byte[] tokenBindingId = null /* set tokenBindingId */;
    ServerProperty serverProperty = new ServerProperty(origin, rpId, challenge, tokenBindingId);

    // expectations
    List<PublicKeyCredentialParameters> pubKeyCredParams = null;
    boolean userVerificationRequired = false;
    boolean userPresenceRequired = true;

    RegistrationParameters registrationParameters =
        new RegistrationParameters(
            serverProperty, pubKeyCredParams, userVerificationRequired, userPresenceRequired);
    webAuthnManager.verify(get_registration_data(), registrationParameters);
  }
}
