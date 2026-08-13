package br.com.f2e.starkbankwebhook.transfer.application;

import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;
import br.com.f2e.starkbankwebhook.transfer.domain.TransferRequest;
import java.util.Objects;

public final class ProcessCreditedInvoice {

  private final TransferIssuer transferIssuer;

  public ProcessCreditedInvoice(TransferIssuer transferIssuer) {
    this.transferIssuer = Objects.requireNonNull(transferIssuer);
  }

  public void execute(CreditedInvoice invoice) {

    var request = new TransferRequest(invoice.invoiceId(), invoice.netAmount());

    transferIssuer.issue(request);
  }
}
