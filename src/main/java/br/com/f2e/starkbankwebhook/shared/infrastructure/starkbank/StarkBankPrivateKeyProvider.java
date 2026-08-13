package br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

final class StarkBankPrivateKeyProvider {

  private final StarkBankProperties properties;

  StarkBankPrivateKeyProvider(StarkBankProperties properties) {
    this.properties = Objects.requireNonNull(properties);
  }

  String get() {
    if (properties.privateKey() != null && !properties.privateKey().isBlank()) {
      return properties.privateKey();
    }

    if (properties.privateKeyFile() == null || properties.privateKeyFile().isBlank()) {
      throw new IllegalStateException(
          "Stark Bank private key or private key file must be configured");
    }

    try {
      return Files.readString(Path.of(properties.privateKeyFile()));
    } catch (IOException exception) {
      throw new IllegalStateException("Failed to read Stark Bank private key file", exception);
    }
  }
}
