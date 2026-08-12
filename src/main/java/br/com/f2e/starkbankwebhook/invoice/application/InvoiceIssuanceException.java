package br.com.f2e.starkbankwebhook.invoice.application;

public final class InvoiceIssuanceException extends RuntimeException {

  public InvoiceIssuanceException(String message, Throwable cause) {
    super(message, cause);
  }
}
