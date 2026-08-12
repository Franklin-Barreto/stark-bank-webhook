package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import com.starkbank.Invoice;
import java.util.Map;
import java.util.Objects;

public final class StarkBankInvoiceMapper {

  public Invoice map(InvoiceDraft draft) {

    Objects.requireNonNull(draft);

    var data =
        Map.<String, Object>of(
            "amount", draft.amount(),
            "name", draft.payerName(),
            "taxId", draft.payerTaxId());

    try {
      return new Invoice(data);
    } catch (Exception exception) {
      throw new IllegalStateException(
          "Failed to map invoice draft to Stark Bank invoice", exception);
    }
  }
}
