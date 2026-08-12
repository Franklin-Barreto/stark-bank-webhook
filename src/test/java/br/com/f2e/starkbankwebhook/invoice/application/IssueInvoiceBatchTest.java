package br.com.f2e.starkbankwebhook.invoice.application;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IssueInvoiceBatchTest {

  @Mock private InvoiceBatchGenerator batchGenerator;

  @Mock private InvoiceIssuer invoiceIssuer;

  private IssueInvoiceBatch useCase;

  @BeforeEach
  void setUp() {
    useCase = new IssueInvoiceBatch(batchGenerator, invoiceIssuer);
  }

  @Test
  void shouldGenerateAndIssueInvoiceBatch() {
    var drafts = getDrafts();

    when(batchGenerator.generate()).thenReturn(drafts);

    useCase.execute();
    verify(invoiceIssuer).issue(drafts);
  }

  private static List<InvoiceDraft> getDrafts() {
    return List.of(
        new InvoiceDraft(5_000, "Jon Snow", "123.456.789-09"),
        new InvoiceDraft(7_500, "Arya Stark", "987.654.321-00"));
  }
}
