package br.com.f2e.starkbankwebhook.transfer.application;

public interface ProcessedInvoiceCreditStore {

  boolean wasProcessed(String invoiceId);

  void markAsProcessed(String invoiceId);
}
