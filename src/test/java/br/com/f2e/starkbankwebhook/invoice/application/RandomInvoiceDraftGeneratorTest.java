package br.com.f2e.starkbankwebhook.invoice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RandomInvoiceDraftGeneratorTest {

  @Mock private RandomGenerator randomGenerator;

  private RandomInvoiceDraftGenerator generator;

  @BeforeEach
  void setUp() {
    generator = new RandomInvoiceDraftGenerator(randomGenerator);
  }

  @Test
  void shouldGenerateInvoiceDraftWithRandomPayerAndAmount() {

    when(randomGenerator.nextInt(10)).thenReturn(1);
    when(randomGenerator.nextLong(10, 101)).thenReturn(50L);

    var invoiceDraft = generator.generate();

    assertThat(invoiceDraft.amount()).isEqualTo(5_000L);
    assertThat(invoiceDraft.payerName()).isEqualTo("Arya Stark");
    assertThat(invoiceDraft.payerTaxId()).isEqualTo("987.654.321-00");
  }
}
