package br.com.f2e.starkbankwebhook.invoice.application;

import java.util.Objects;

public class IssueInvoiceBatch {

  private final InvoiceBatchGenerator batchGenerator;
  private final InvoiceIssuer invoiceIssuer;

  public IssueInvoiceBatch(InvoiceBatchGenerator batchGenerator, InvoiceIssuer invoiceIssuer) {
    this.batchGenerator = Objects.requireNonNull(batchGenerator);
    this.invoiceIssuer = Objects.requireNonNull(invoiceIssuer);
  }

  public void execute() {
    invoiceIssuer.issue(batchGenerator.generate());
  }
}
