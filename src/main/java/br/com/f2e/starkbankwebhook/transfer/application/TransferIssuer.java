package br.com.f2e.starkbankwebhook.transfer.application;

import br.com.f2e.starkbankwebhook.transfer.domain.TransferRequest;

public interface TransferIssuer {

  void issue(TransferRequest request);
}
