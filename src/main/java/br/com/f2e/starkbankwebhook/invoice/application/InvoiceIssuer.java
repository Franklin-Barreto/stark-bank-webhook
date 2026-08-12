package br.com.f2e.starkbankwebhook.invoice.application;

import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import java.util.List;

public interface InvoiceIssuer {

  void issue(List<InvoiceDraft> drafts);
}
