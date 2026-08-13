package br.com.f2e.starkbankwebhook.shared.infrastructure.persistence.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class PostgresTestContainerConfiguration {

  private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17-alpine");

  @Bean(destroyMethod = "")
  @ServiceConnection
  public PostgreSQLContainer postgresContainer() {
    return POSTGRES;
  }
}
