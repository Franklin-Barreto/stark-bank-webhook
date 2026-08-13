package br.com.f2e.starkbankwebhook.transfer.application;

import java.util.Objects;

public class ReconcileInvoiceCredits {

  private final EventService eventService;
  private final ProcessCreditedInvoice processCreditedInvoice;

  public ReconcileInvoiceCredits(
      EventService eventService, ProcessCreditedInvoice processCreditedInvoice) {
    this.eventService = Objects.requireNonNull(eventService);
    this.processCreditedInvoice = Objects.requireNonNull(processCreditedInvoice);
  }

  public void execute() {
    for (var event : this.eventService.findUndeliveredInvoiceCredits()) {
      processCreditedInvoice.execute(event.creditedInvoice());
      eventService.markAsDelivered(event.eventId());
    }
  }
}
