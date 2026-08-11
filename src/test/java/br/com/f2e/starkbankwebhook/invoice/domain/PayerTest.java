package br.com.f2e.starkbankwebhook.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PayerTest {

  @Test
  void shouldCreatePayerWithNormalizedData() {
    var payer = new Payer("  Payer   ", "  taxId   ");

    assertThat(payer.name()).isEqualTo("Payer");
    assertThat(payer.taxId()).isEqualTo("taxId");
  }

  @ParameterizedTest
  @MethodSource("invalidPayerArguments")
  void shouldRejectInvalidArguments(String payerName, String payerTaxId, String expectedMessage) {

    assertThatThrownBy(() -> new Payer(payerName, payerTaxId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(expectedMessage);
  }

  private static Stream<Arguments> invalidPayerArguments() {
    return Stream.of(
        Arguments.of(null, "taxId", "name must not be null"),
        Arguments.of("", "taxId", "name must not be blank"),
        Arguments.of(" ", "taxId", "name must not be blank"),
        Arguments.of("Payer", null, "taxId must not be null"),
        Arguments.of("Payer", "", "taxId must not be blank"),
        Arguments.of("Payer", " ", "taxId must not be blank"));
  }
}
