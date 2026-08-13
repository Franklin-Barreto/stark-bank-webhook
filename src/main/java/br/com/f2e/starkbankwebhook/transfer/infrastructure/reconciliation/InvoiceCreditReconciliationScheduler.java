package br.com.f2e.starkbankwebhook.transfer.infrastructure.reconciliation;

import br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank.ConditionalOnStarkBankEnabled;
import br.com.f2e.starkbankwebhook.transfer.application.ReconcileInvoiceCredits;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnStarkBankEnabled
@ConditionalOnProperty(
    prefix = "starkbank.event-reconciliation.scheduling",
    name = "enabled",
    havingValue = "true")
class InvoiceCreditReconciliationScheduler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(InvoiceCreditReconciliationScheduler.class);

  private final ReconcileInvoiceCredits reconcileInvoiceCredits;

  InvoiceCreditReconciliationScheduler(ReconcileInvoiceCredits reconcileInvoiceCredits) {
    this.reconcileInvoiceCredits = Objects.requireNonNull(reconcileInvoiceCredits);
  }

  @Scheduled(
      fixedDelayString = "${starkbank.event-reconciliation.scheduling.interval}",
      initialDelayString = "${starkbank.event-reconciliation.scheduling.interval}")
  void reconcile() {
    LOGGER.info("Starting scheduled invoice credit reconciliation");
    reconcileInvoiceCredits.execute();
    LOGGER.info("Scheduled invoice credit reconciliation finished");
  }
}
