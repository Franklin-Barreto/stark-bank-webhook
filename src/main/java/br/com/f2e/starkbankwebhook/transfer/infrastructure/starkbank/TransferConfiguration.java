package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank.ConditionalOnStarkBankEnabled;
import br.com.f2e.starkbankwebhook.transfer.application.EventService;
import br.com.f2e.starkbankwebhook.transfer.application.ProcessCreditedInvoice;
import br.com.f2e.starkbankwebhook.transfer.application.ReconcileInvoiceCredits;
import br.com.f2e.starkbankwebhook.transfer.application.TransferIssuer;
import com.starkbank.Project;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnStarkBankEnabled
class TransferConfiguration {

  @Bean
  StarkBankTransferMapper starkBankTransferMapper() {
    return new StarkBankTransferMapper();
  }

  @Bean
  StarkBankTransferGateway starkBankTransferGateway(Project project) {
    return new SdkStarkBankTransferGateway(project);
  }

  @Bean
  TransferIssuer transferIssuer(StarkBankTransferMapper mapper, StarkBankTransferGateway gateway) {
    return new StarkBankTransferIssuer(mapper, gateway);
  }

  @Bean
  ProcessCreditedInvoice processCreditedInvoice(TransferIssuer transferIssuer) {
    return new ProcessCreditedInvoice(transferIssuer);
  }

  @Bean
  EventService eventService(Project project) {
    return new StarkBankEventService(project);
  }

  @Bean
  ReconcileInvoiceCredits reconcileInvoiceCredits(
      EventService eventService, ProcessCreditedInvoice processCreditedInvoice) {
    return new ReconcileInvoiceCredits(eventService, processCreditedInvoice);
  }
}
