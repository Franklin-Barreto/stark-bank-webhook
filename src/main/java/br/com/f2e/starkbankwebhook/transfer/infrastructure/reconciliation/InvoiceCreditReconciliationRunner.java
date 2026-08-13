package br.com.f2e.starkbankwebhook.transfer.infrastructure.reconciliation;

import br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank.ConditionalOnStarkBankEnabled;
import br.com.f2e.starkbankwebhook.transfer.application.ReconcileInvoiceCredits;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnStarkBankEnabled
@ConditionalOnProperty(
    prefix = "starkbank.event-reconciliation",
    name = "run-on-startup",
    havingValue = "true")
public class InvoiceCreditReconciliationRunner implements ApplicationRunner {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(InvoiceCreditReconciliationRunner.class);

  private final ReconcileInvoiceCredits reconcileInvoiceCredits;

  public InvoiceCreditReconciliationRunner(ReconcileInvoiceCredits reconcileInvoiceCredits) {
    this.reconcileInvoiceCredits = reconcileInvoiceCredits;
  }

  @Override
  public void run(@NonNull ApplicationArguments ignored) {
    LOGGER.info("Starting invoice credit reconciliation");
    reconcileInvoiceCredits.execute();
    LOGGER.info("Invoice credit reconciliation finished");
  }
}
