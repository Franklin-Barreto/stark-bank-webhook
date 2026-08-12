package br.com.f2e.starkbankwebhook.transfer.infrastructure.webhook;

import br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank.ConditionalOnStarkBankEnabled;
import com.starkbank.Project;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnStarkBankEnabled
public class WebhookConfiguration {

  @Bean
  StarkBankWebhookParser starkBankWebhookParser(Project project) {
    return new StarkBankWebhookParser(project);
  }
}
