package br.com.f2e.starkbankwebhook.transfer.infrastructure.webhook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;
import com.starkbank.Event;
import com.starkbank.Invoice;
import com.starkbank.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StarkBankWebhookParserTest {

  private Project project;
  private StarkBankWebhookParser parser;

  @BeforeEach
  void setUp() {
    project = mock(Project.class);
    parser = new StarkBankWebhookParser(project);
  }

  @Test
  void shouldParseCreditedInvoice() {
    var invoice = new Invoice();
    invoice.id = "invoice-id";
    invoice.amount = 10_000;
    invoice.fee = 500;

    var log = new Invoice.Log();
    log.type = "credited";
    log.invoice = invoice;

    var event = new Event.InvoiceEvent();
    event.log = log;

    try (var mockedEvent = mockStatic(Event.class)) {
      mockedEvent.when(() -> Event.parse("payload", "signature", project)).thenReturn(event);

      var result = parser.parse("payload", "signature");

      assertThat(result).contains(new CreditedInvoice("invoice-id", 10_000, 500));
    }
  }

  @Test
  void shouldIgnoreUnrelatedEvent() {
    var event = new Event.UnknownEvent();

    try (var mockedEvent = mockStatic(Event.class)) {
      mockedEvent.when(() -> Event.parse("payload", "signature", project)).thenReturn(event);

      assertThat(parser.parse("payload", "signature")).isEmpty();
    }
  }

  @Test
  void shouldRejectInvalidWebhook() {
    var cause = new Exception("invalid signature");

    try (var mockedEvent = mockStatic(Event.class)) {
      mockedEvent.when(() -> Event.parse("payload", "signature", project)).thenThrow(cause);

      assertThatThrownBy(() -> parser.parse("payload", "signature"))
          .isInstanceOf(InvalidWebhookException.class)
          .hasMessage("Invalid Stark Bank webhook")
          .hasCause(cause);
    }
  }
}
