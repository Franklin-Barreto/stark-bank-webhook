package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.invoice.application.InvoiceBatchGenerator;
import br.com.f2e.starkbankwebhook.invoice.application.InvoiceIssuer;
import br.com.f2e.starkbankwebhook.invoice.application.IssueInvoiceBatch;
import br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank.ConditionalOnStarkBankEnabled;
import com.starkbank.Project;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnStarkBankEnabled
class StarkBankInvoiceConfiguration {

  @Bean
  StarkBankInvoiceMapper starkBankInvoiceMapper() {
    return new StarkBankInvoiceMapper();
  }

  @Bean
  StarkBankInvoiceGateway starkBankInvoiceGateway(Project project) {
    return new SdkStarkBankInvoiceGateway(project);
  }

  @Bean
  InvoiceIssuer invoiceIssuer(StarkBankInvoiceMapper mapper, StarkBankInvoiceGateway gateway) {
    return new StarkBankInvoiceIssuer(mapper, gateway);
  }

  @Bean
  IssueInvoiceBatch issueInvoiceBatch(
      InvoiceBatchGenerator invoiceBatchGenerator, InvoiceIssuer invoiceIssuer) {
    return new IssueInvoiceBatch(invoiceBatchGenerator, invoiceIssuer);
  }
}
