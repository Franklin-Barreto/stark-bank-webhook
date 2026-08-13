package br.com.f2e.starkbankwebhook.transfer.domain;

import static br.com.f2e.starkbankwebhook.shared.domain.validation.Preconditions.requireNotBlank;
import static br.com.f2e.starkbankwebhook.shared.domain.validation.Preconditions.requirePositive;

public record TransferRequest(String externalId, long amount) {

  public TransferRequest {
    externalId = requireNotBlank(externalId, "externalId");
    requirePositive(amount, "amount");
  }
}
