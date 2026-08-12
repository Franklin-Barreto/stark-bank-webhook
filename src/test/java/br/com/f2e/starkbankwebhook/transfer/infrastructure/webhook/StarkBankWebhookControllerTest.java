package br.com.f2e.starkbankwebhook.transfer.infrastructure.webhook;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StarkBankWebhookControllerTest {

  @Mock private StarkBankWebhookParser webhookParser;

  private StarkBankWebhookController controller;

  @BeforeEach
  void setUp() {
    controller = new StarkBankWebhookController(webhookParser);
  }

  @Test
  void shouldParseWebhookPayload() {
    var payload = "payload";
    var signature = "signature";

    controller.handle(signature, payload);

    verify(webhookParser).parse(payload, signature);
  }
}
