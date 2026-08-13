package br.com.f2e.starkbankwebhook.transfer.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReconcileInvoiceCreditsTest {

  @Mock private EventService eventService;
  @Mock private ProcessCreditedInvoice processCreditedInvoice;

  private ReconcileInvoiceCredits reconcileInvoiceCredits;

  @BeforeEach
  void setUp() {
    reconcileInvoiceCredits = new ReconcileInvoiceCredits(eventService, processCreditedInvoice);
  }

  @Test
  void shouldProcessUndeliveredInvoiceCreditAndMarkEventAsDelivered() {
    var event = new InvoiceCreditEvent("event-id", new CreditedInvoice("invoice-id", 1000, 500));
    when(eventService.findUndeliveredInvoiceCredits()).thenReturn(List.of(event));

    reconcileInvoiceCredits.execute();
    var order = inOrder(processCreditedInvoice, eventService);

    order.verify(processCreditedInvoice).execute(event.creditedInvoice());
    order.verify(eventService).markAsDelivered(event.eventId());
  }

  @Test
  void shouldNotMarkEventAsDeliveredWhenProcessingFails() {
    var event = new InvoiceCreditEvent("event-id", new CreditedInvoice("invoice-id", 1000, 500));

    when(eventService.findUndeliveredInvoiceCredits()).thenReturn(List.of(event));

    doThrow(new RuntimeException("transfer failed"))
        .when(processCreditedInvoice)
        .execute(event.creditedInvoice());

    assertThatThrownBy(reconcileInvoiceCredits::execute)
        .isInstanceOf(RuntimeException.class)
        .hasMessage("transfer failed");

    verify(eventService, never()).markAsDelivered(event.eventId());
  }
}
