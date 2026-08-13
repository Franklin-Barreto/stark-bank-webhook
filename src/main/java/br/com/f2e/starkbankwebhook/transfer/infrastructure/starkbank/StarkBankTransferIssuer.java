package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.transfer.application.TransferIssuer;
import br.com.f2e.starkbankwebhook.transfer.domain.TransferRequest;
import java.util.List;
import java.util.Objects;

public final class StarkBankTransferIssuer implements TransferIssuer {

  private final StarkBankTransferMapper mapper;
  private final StarkBankTransferGateway gateway;

  public StarkBankTransferIssuer(StarkBankTransferMapper mapper, StarkBankTransferGateway gateway) {
    this.mapper = Objects.requireNonNull(mapper);
    this.gateway = Objects.requireNonNull(gateway);
  }

  @Override
  public void issue(TransferRequest request) {
    gateway.create(List.of(mapper.map(request)));
  }
}
