package br.com.f2e.starkbankwebhook.invoice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceBatchGeneratorTest {

  @Mock private RandomInvoiceBatchSizeGenerator batchSizeGenerator;

  @Mock private RandomInvoiceDraftGenerator draftGenerator;

  private InvoiceBatchGenerator generator;

  @BeforeEach
  void setUp() {
    generator = new InvoiceBatchGenerator(batchSizeGenerator, draftGenerator);
  }

  @Test
  void shouldGenerateBatchWithRequestedSize() {

    var draft1 = new InvoiceDraft(100, "Jon Snow", "123.456.789-09");
    var draft2 = new InvoiceDraft(100, "Arya Stark", "987.654.321-00");
    var draft3 = new InvoiceDraft(100, "Sansa Stark", "529.982.247-25");

    when(batchSizeGenerator.generate()).thenReturn(3);
    when(draftGenerator.generate()).thenReturn(draft1, draft2, draft3);

    var batch = generator.generate();

    assertThat(batch).containsExactly(draft1, draft2, draft3);
    verify(batchSizeGenerator).generate();
  }
}
