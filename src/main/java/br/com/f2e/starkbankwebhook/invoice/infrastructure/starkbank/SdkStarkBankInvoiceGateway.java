package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.invoice.application.InvoiceIssuanceException;
import com.starkbank.Invoice;
import com.starkbank.Project;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SdkStarkBankInvoiceGateway implements StarkBankInvoiceGateway {

  private static final Logger LOGGER = LoggerFactory.getLogger(SdkStarkBankInvoiceGateway.class);

  private final Project project;

  SdkStarkBankInvoiceGateway(Project project) {
    this.project = Objects.requireNonNull(project);
  }

  @Override
  public void create(List<Invoice> invoices) {
    try {
      LOGGER.info("Issuing invoice batch: size={}", invoices.size());
      var createdInvoices = Invoice.create(invoices, project);

      LOGGER.info(
          "Invoice batch issued successfully: requested={}, created={}",
          invoices.size(),
          createdInvoices.size());
    } catch (Exception exception) {
      throw new InvoiceIssuanceException("Failed to issue invoices through Stark Bank", exception);
    }
  }
}
