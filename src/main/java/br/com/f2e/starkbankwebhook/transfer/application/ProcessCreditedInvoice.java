package br.com.f2e.starkbankwebhook.transfer.application;

import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;
import br.com.f2e.starkbankwebhook.transfer.domain.TransferRequest;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProcessCreditedInvoice {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCreditedInvoice.class);

  private final TransferIssuer transferIssuer;
  private final ProcessedInvoiceCreditStore processedInvoiceCreditStore;

  public ProcessCreditedInvoice(
      TransferIssuer transferIssuer, ProcessedInvoiceCreditStore processedInvoiceCreditStore) {
    this.transferIssuer = Objects.requireNonNull(transferIssuer);
    this.processedInvoiceCreditStore = Objects.requireNonNull(processedInvoiceCreditStore);
  }

  public void execute(CreditedInvoice invoice) {
    if (!processedInvoiceCreditStore.tryClaim(invoice.invoiceId())) {
      LOGGER.info("Invoice credit already claimed: invoiceId={}", invoice.invoiceId());
      return;
    }

    var request = new TransferRequest(invoice.invoiceId(), invoice.netAmount());

    try {
      transferIssuer.issue(request);
    } catch (RuntimeException exception) {
      processedInvoiceCreditStore.releaseClaim(invoice.invoiceId());
      throw exception;
    }
  }
}
