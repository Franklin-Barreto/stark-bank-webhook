package br.com.f2e.starkbankwebhook.transfer.infrastructure.webhook;

import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;
import com.starkbank.Event;
import com.starkbank.Project;
import java.util.Objects;
import java.util.Optional;

public class StarkBankWebhookParser {

  private final Project project;

  public StarkBankWebhookParser(Project project) {
    this.project = Objects.requireNonNull(project);
  }

  public Optional<CreditedInvoice> parse(String payload, String signature) {
    try {
      var event = Event.parse(payload, signature, project);

      if (!(event instanceof Event.InvoiceEvent invoiceEvent)
          || !"credited".equals(invoiceEvent.log.type)) return Optional.empty();
      var invoice = invoiceEvent.log.invoice;
      var creditedInvoice =
          new CreditedInvoice(
              invoice.id,
              invoice.amount.longValue(),
              invoice.fee == null ? 0 : invoice.fee.longValue());

      return Optional.of(creditedInvoice);

    } catch (Exception e) {
      throw new InvalidWebhookException(e);
    }
  }
}
