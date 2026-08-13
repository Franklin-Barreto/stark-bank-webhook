package br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank;

import com.starkbank.Project;
import com.starkcore.Settings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StarkBankProperties.class)
@ConditionalOnStarkBankEnabled
class StarkBankClientConfiguration {

  @Bean
  Project starkBankProject(StarkBankProperties properties) {
    try {
      var project =
          new Project(properties.environment(), properties.projectId(), properties.privateKey());

      configureSdkUser(project);
      return project;
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to configure Stark Bank project", exception);
    }
  }

  private static void configureSdkUser(Project project) {
    // Event.parse relies on the SDK's global user in SDK 2.25.2.
    Settings.user = project;
  }
}
