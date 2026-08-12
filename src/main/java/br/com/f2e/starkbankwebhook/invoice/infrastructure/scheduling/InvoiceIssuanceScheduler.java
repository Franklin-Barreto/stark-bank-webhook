package br.com.f2e.starkbankwebhook.invoice.infrastructure.scheduling;

import br.com.f2e.starkbankwebhook.invoice.application.IssueInvoiceBatch;
import java.util.Objects;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    prefix = "starkbank.invoice.scheduling",
    name = "enabled",
    havingValue = "true")
class InvoiceIssuanceScheduler {

  private final IssueInvoiceBatch issueInvoiceBatch;

  InvoiceIssuanceScheduler(IssueInvoiceBatch issueInvoiceBatch) {
    this.issueInvoiceBatch = Objects.requireNonNull(issueInvoiceBatch);
  }

  @Scheduled(fixedRateString = "${starkbank.invoice.scheduling.interval}")
  void issueInvoices() {
    issueInvoiceBatch.execute();
  }
}
