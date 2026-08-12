package br.com.f2e.starkbankwebhook.invoice.infrastructure.config;

import br.com.f2e.starkbankwebhook.invoice.application.InvoiceBatchGenerator;
import br.com.f2e.starkbankwebhook.invoice.application.RandomInvoiceBatchSizeGenerator;
import br.com.f2e.starkbankwebhook.invoice.application.RandomInvoiceDraftGenerator;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InvoiceConfiguration {

  @Bean
  public RandomGenerator randomGenerator() {
    return new Random();
  }

  @Bean
  public RandomInvoiceBatchSizeGenerator randomInvoiceBatchSizeGenerator(
      RandomGenerator randomGenerator) {
    return new RandomInvoiceBatchSizeGenerator(randomGenerator);
  }

  @Bean
  public RandomInvoiceDraftGenerator randomInvoiceDraftGenerator(RandomGenerator randomGenerator) {
    return new RandomInvoiceDraftGenerator(randomGenerator);
  }

  @Bean
  public InvoiceBatchGenerator invoiceBatchGenerator(
      RandomInvoiceBatchSizeGenerator batchSizeGenerator,
      RandomInvoiceDraftGenerator draftGenerator) {
    return new InvoiceBatchGenerator(batchSizeGenerator, draftGenerator);
  }
}
