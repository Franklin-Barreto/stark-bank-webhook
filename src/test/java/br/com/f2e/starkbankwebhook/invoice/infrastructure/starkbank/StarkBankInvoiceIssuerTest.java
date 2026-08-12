package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import com.starkbank.Invoice;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StarkBankInvoiceIssuerTest {

  @Mock private StarkBankInvoiceMapper mapper;

  @Mock private StarkBankInvoiceGateway gateway;

  private StarkBankInvoiceIssuer issuer;

  @BeforeEach
  void setUp() {
    issuer = new StarkBankInvoiceIssuer(mapper, gateway);
  }

  @Test
  void shouldMapAndIssueInvoiceBatch() {

    var starkInvoice1 = new Invoice();
    var starkInvoice2 = new Invoice();

    var drafts = generateDrafts();
    var draft1 = drafts.get(0);
    var draft2 = drafts.get(1);

    when(mapper.map(draft1)).thenReturn(starkInvoice1);
    when(mapper.map(draft2)).thenReturn(starkInvoice2);

    issuer.issue(drafts);

    verify(gateway).create(List.of(starkInvoice1, starkInvoice2));
  }

  private static List<InvoiceDraft> generateDrafts() {
    return List.of(
        new InvoiceDraft(5_000, "Jon Snow", "123.456.789-09"),
        new InvoiceDraft(7_500, "Arya Stark", "987.654.321-00"));
  }
}
