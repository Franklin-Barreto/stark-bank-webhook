package br.com.f2e.starkbankwebhook.invoice.domain;

import static br.com.f2e.starkbankwebhook.invoice.domain.Preconditions.requireNotBlank;
import static br.com.f2e.starkbankwebhook.invoice.domain.Preconditions.requirePositive;

public record InvoiceDraft(long amount, String payerName, String payerTaxId) {

  public InvoiceDraft {
    requirePositive(amount, "amount");
    payerName = requireNotBlank(payerName, "payer name");
    payerTaxId = requireNotBlank(payerTaxId, "payer tax id");
  }
}
