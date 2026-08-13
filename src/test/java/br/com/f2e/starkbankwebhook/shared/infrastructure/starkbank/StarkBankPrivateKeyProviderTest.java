package br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class StarkBankPrivateKeyProviderTest {

  @TempDir private Path temporaryDirectory;

  @Test
  void shouldUseConfiguredPrivateKey() {
    var provider = provider("private-key", "ignored-file");

    assertThat(provider.get()).isEqualTo("private-key");
  }

  @Test
  void shouldReadPrivateKeyFromConfiguredFile() throws Exception {
    var privateKeyFile = temporaryDirectory.resolve("private-key.pem");
    Files.writeString(privateKeyFile, "private-key-from-file");
    var provider = provider("", privateKeyFile.toString());

    assertThat(provider.get()).isEqualTo("private-key-from-file");
  }

  @Test
  void shouldRejectMissingPrivateKeyConfiguration() {
    var provider = provider("", "");

    assertThatThrownBy(provider::get)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Stark Bank private key or private key file must be configured");
  }

  private static StarkBankPrivateKeyProvider provider(String privateKey, String privateKeyFile) {
    var properties =
        new StarkBankProperties(true, "sandbox", "project-id", privateKey, privateKeyFile);
    return new StarkBankPrivateKeyProvider(properties);
  }
}
