package br.com.f2e.starkbankwebhook.transfer.application;

import java.util.List;

public interface EventService {

  List<InvoiceCreditEvent> findUndeliveredInvoiceCredits();

  void markAsDelivered(String eventId);
}
