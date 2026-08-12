package br.com.f2e.starkbankwebhook.invoice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RandomInvoiceBatchSizeGeneratorTest {

  @Mock private RandomGenerator randomGenerator;

  private RandomInvoiceBatchSizeGenerator batchSizeGenerator;

  @BeforeEach
  void setUp() {
    batchSizeGenerator = new RandomInvoiceBatchSizeGenerator(randomGenerator);
  }

  @ParameterizedTest
  @ValueSource(ints = {8, 9, 10, 11, 12})
  void shouldGenerateBatchSizeBetweenEightAndTwelve(int expectedSize) {
    when(randomGenerator.nextInt(8, 13)).thenReturn(expectedSize);

    int generatedSize = batchSizeGenerator.generate();

    assertThat(generatedSize).isEqualTo(expectedSize);
  }
}
