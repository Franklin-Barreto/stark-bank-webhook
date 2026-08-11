package br.com.f2e.starkbankwebhook.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class InvoiceDraftTest {

  @Test
  void shouldCreateInvoiceDraftWithValidData() {
    var invoiceDraft = new InvoiceDraft(2000, "Payer", "PayerTaxId");

    assertThat(invoiceDraft.amount()).isEqualTo(2000);
    assertThat(invoiceDraft.payerTaxId()).isEqualTo("PayerTaxId");
    assertThat(invoiceDraft.payerName()).isEqualTo("Payer");
  }

  @Test
  void shouldTrimPayerNameAndTaxId() {
    var invoiceDraft = new InvoiceDraft(2000, "  Payer  ", "  PayerTaxId  ");

    assertThat(invoiceDraft.payerName()).isEqualTo("Payer");
    assertThat(invoiceDraft.payerTaxId()).isEqualTo("PayerTaxId");
  }

  @ParameterizedTest
  @ValueSource(longs = {-100, -1, 0})
  void shouldRejectNonPositiveAmount(long invalidAmount) {

    assertThatThrownBy(() -> new InvoiceDraft(invalidAmount, "Payer", "payer tax id"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("amount must be greater than zero");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t"})
  void shouldRejectBlankPayerName(String invalidName) {
    assertThatThrownBy(() -> new InvoiceDraft(2000, invalidName, "PayerTaxId"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageStartingWith("payer name must not be");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t"})
  void shouldRejectBlankPayerTaxId(String invalidTaxId) {
    assertThatThrownBy(() -> new InvoiceDraft(2000, "Payer", invalidTaxId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageStartingWith("payer tax id must not be");
  }
}
