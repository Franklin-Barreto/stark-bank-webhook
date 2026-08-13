package br.com.f2e.starkbankwebhook.transfer.application;

public class EventReconciliationException extends RuntimeException {

  public EventReconciliationException(String message, Exception exception) {
    super(message, exception);
  }
}
