package br.com.f2e.starkbankwebhook.invoice.domain;

import static br.com.f2e.starkbankwebhook.shared.domain.validation.Preconditions.requireNotBlank;

public record Payer(String name, String taxId) {

  public Payer {
    name = requireNotBlank(name, "name");
    taxId = requireNotBlank(taxId, "taxId");
  }
}
