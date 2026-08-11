package br.com.f2e.starkbankwebhook;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
@SpringBootTest
class StarkBankWebhookApplicationTests {

  @Container @ServiceConnection
  static final PostgreSQLContainer POSTGRESQL =
      new PostgreSQLContainer("postgres:17-alpine")
          .withDatabaseName("starkbank_test")
          .withUsername("starkbank")
          .withPassword("starkbank");

  @Test
  void contextLoads() {}
}
