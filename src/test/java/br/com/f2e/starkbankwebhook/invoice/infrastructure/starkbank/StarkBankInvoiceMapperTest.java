package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import com.starkbank.Invoice;
import org.junit.jupiter.api.Test;

class StarkBankInvoiceMapperTest {

  private StarkBankInvoiceMapper starkBankInvoiceMapper = new StarkBankInvoiceMapper();

  @Test
  void shouldMapInvoiceDraftToStarkBankInvoice() {
    var invoiceDraft = new InvoiceDraft(5_000, "Jon Snow", "123.456.789-09");
    Invoice startBankInvoice = starkBankInvoiceMapper.map(invoiceDraft);

    assertThat(invoiceDraft.amount()).isEqualTo(startBankInvoice.amount);
    assertThat(invoiceDraft.payerName()).isEqualTo(startBankInvoice.name);
    assertThat(invoiceDraft.payerTaxId()).isEqualTo(startBankInvoice.taxId);
  }
}
