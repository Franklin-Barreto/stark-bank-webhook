package br.com.f2e.starkbankwebhook.transfer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CreditedInvoiceTest {

  @Test
  void shouldCalculateNetAmount() {

    var creditedInvoice = new CreditedInvoice("invoice-id", 10_000, 500);

    assertThat(creditedInvoice.netAmount()).isEqualTo(9_500);
  }

  @ParameterizedTest
  @ValueSource(longs = {10_000, 10_001})
  void shouldRejectFeeEqualToOrGreaterThanAmount(long invalidFee) {

    assertThatThrownBy(() -> new CreditedInvoice("invoice-id", 10_000, invalidFee))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("fee must be less than amount");
  }
}
