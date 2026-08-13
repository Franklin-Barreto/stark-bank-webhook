package br.com.f2e.starkbankwebhook.transfer.application;

public final class TransferIssuanceException extends RuntimeException {

  public TransferIssuanceException(String message, Throwable cause) {
    super(message, cause);
  }
}
