package br.com.f2e.starkbankwebhook.transfer.domain;

import static br.com.f2e.starkbankwebhook.shared.domain.validation.Preconditions.requireNonNegative;
import static br.com.f2e.starkbankwebhook.shared.domain.validation.Preconditions.requireNotBlank;
import static br.com.f2e.starkbankwebhook.shared.domain.validation.Preconditions.requirePositive;

public record CreditedInvoice(String invoiceId, long amount, long fee) {

  public CreditedInvoice {
    invoiceId = requireNotBlank(invoiceId, "invoiceId");
    requirePositive(amount, "amount");
    requireNonNegative(fee, "fee");
    if (fee >= amount) {
      throw new IllegalArgumentException("fee must be less than amount");
    }
  }

  public long netAmount() {
    return amount - fee;
  }
}
