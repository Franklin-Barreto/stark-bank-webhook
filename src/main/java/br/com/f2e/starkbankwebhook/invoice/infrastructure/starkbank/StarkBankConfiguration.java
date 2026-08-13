package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.invoice.application.InvoiceBatchGenerator;
import br.com.f2e.starkbankwebhook.invoice.application.InvoiceIssuer;
import br.com.f2e.starkbankwebhook.invoice.application.IssueInvoiceBatch;
import br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank.ConditionalOnStarkBankEnabled;
import com.starkbank.Project;
import com.starkcore.Settings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(StarkBankProperties.class)
@ConditionalOnStarkBankEnabled
class StarkBankConfiguration {

  @Bean
  Project starkBankProject(StarkBankProperties properties) {
    try {
      var project =
          new Project(properties.environment(), properties.projectId(), properties.privateKey());

      Settings.user = project;
      return project;
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to configure Stark Bank project", exception);
    }
  }

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
