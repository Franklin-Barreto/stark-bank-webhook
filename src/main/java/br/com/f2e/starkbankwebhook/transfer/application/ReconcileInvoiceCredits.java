package br.com.f2e.starkbankwebhook.transfer.application;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReconcileInvoiceCredits {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReconcileInvoiceCredits.class);

  private final EventService eventService;
  private final ProcessCreditedInvoice processCreditedInvoice;

  public ReconcileInvoiceCredits(
      EventService eventService, ProcessCreditedInvoice processCreditedInvoice) {
    this.eventService = Objects.requireNonNull(eventService);
    this.processCreditedInvoice = Objects.requireNonNull(processCreditedInvoice);
  }

  public void execute() {
    var events = eventService.findUndeliveredInvoiceCredits();

    LOGGER.info("Undelivered invoice credit events found: count={}", events.size());

    for (var event : events) {
      LOGGER.info(
          "Reconciling invoice credit: eventId={}, invoiceId={}",
          event.eventId(),
          event.creditedInvoice().invoiceId());

      processCreditedInvoice.execute(event.creditedInvoice());
      eventService.markAsDelivered(event.eventId());

      LOGGER.info(
          "Invoice credit reconciled: eventId={}, invoiceId={}",
          event.eventId(),
          event.creditedInvoice().invoiceId());
    }
  }
}
