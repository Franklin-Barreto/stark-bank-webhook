package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.transfer.application.EventReconciliationException;
import br.com.f2e.starkbankwebhook.transfer.application.EventService;
import br.com.f2e.starkbankwebhook.transfer.application.InvoiceCreditEvent;
import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;
import com.starkbank.Event;
import com.starkbank.Project;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class StarkBankEventService implements EventService {

  private final Project project;

  public StarkBankEventService(Project project) {
    this.project = Objects.requireNonNull(project);
  }

  @Override
  public List<InvoiceCreditEvent> findUndeliveredInvoiceCredits() {
    var params = Map.<String, Object>of("isDelivered", false);

    try {
      return StreamSupport.stream(Event.query(params, project).spliterator(), false)
          .filter(event -> "invoice".equals(event.subscription))
          .filter(Event.InvoiceEvent.class::isInstance)
          .map(Event.InvoiceEvent.class::cast)
          .filter(event -> "credited".equals(event.log.type))
          .map(this::map)
          .toList();
    } catch (Exception exception) {
      throw new EventReconciliationException(
          "Failed to query undelivered Stark Bank invoice events", exception);
    }
  }

  @Override
  public void markAsDelivered(String eventId) {
    var params = Map.<String, Object>of("isDelivered", true);
    try {
      Event.update(eventId, params, project);
    } catch (Exception exception) {
      throw new EventReconciliationException(
          "Failed to mark Stark Bank event as delivered", exception);
    }
  }

  private InvoiceCreditEvent map(Event.InvoiceEvent event) {
    var invoice = event.log.invoice;

    var creditedInvoice =
        new CreditedInvoice(
            invoice.id,
            invoice.amount.longValue(),
            invoice.fee == null ? 0L : invoice.fee.longValue());

    return new InvoiceCreditEvent(event.id, creditedInvoice);
  }
}
