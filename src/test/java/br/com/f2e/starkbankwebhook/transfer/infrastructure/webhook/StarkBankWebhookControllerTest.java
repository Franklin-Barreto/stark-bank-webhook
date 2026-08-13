package br.com.f2e.starkbankwebhook.transfer.infrastructure.webhook;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import br.com.f2e.starkbankwebhook.transfer.application.ProcessCreditedInvoice;
import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StarkBankWebhookControllerTest {

  @Mock private StarkBankWebhookParser webhookParser;
  @Mock private ProcessCreditedInvoice processCreditedInvoice;

  private StarkBankWebhookController controller;

  @BeforeEach
  void setUp() {
    controller = new StarkBankWebhookController(webhookParser, processCreditedInvoice);
  }

  @Test
  void shouldProcessCreditedInvoice() {
    var payload = "payload";
    var signature = "signature";
    var creditedInvoice = new CreditedInvoice("invoice-id", 10_000, 500);

    org.mockito.Mockito.when(webhookParser.parse(payload, signature))
        .thenReturn(Optional.of(creditedInvoice));

    controller.handle(signature, payload);

    verify(processCreditedInvoice).execute(creditedInvoice);
  }

  @Test
  void shouldIgnoreUnrelatedWebhookEvent() {
    var payload = "payload";
    var signature = "signature";

    org.mockito.Mockito.when(webhookParser.parse(payload, signature)).thenReturn(Optional.empty());

    controller.handle(signature, payload);

    verify(processCreditedInvoice, never()).execute(any());
  }
}
