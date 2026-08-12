package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.invoice.application.InvoiceIssuer;
import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import com.starkbank.Invoice;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StarkBankInvoiceIssuer implements InvoiceIssuer {

  private final StarkBankInvoiceMapper mapper;
  private final StarkBankInvoiceGateway gateway;

  StarkBankInvoiceIssuer(StarkBankInvoiceMapper mapper, StarkBankInvoiceGateway gateway) {
    this.mapper = Objects.requireNonNull(mapper);
    this.gateway = Objects.requireNonNull(gateway);
  }

  @Override
  public void issue(List<InvoiceDraft> drafts) {
    var invoices = new ArrayList<Invoice>(drafts.size());

    for (var draft : drafts) {
      invoices.add(mapper.map(draft));
    }
    gateway.create(invoices);
  }
}
