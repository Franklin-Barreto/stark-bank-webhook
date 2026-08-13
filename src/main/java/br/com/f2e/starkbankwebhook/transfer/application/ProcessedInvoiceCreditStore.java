package br.com.f2e.starkbankwebhook.transfer.application;

public interface ProcessedInvoiceCreditStore {

  boolean tryClaim(String invoiceId);

  void releaseClaim(String invoiceId);
}
