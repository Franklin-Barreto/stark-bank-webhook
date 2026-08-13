package br.com.f2e.starkbankwebhook.transfer.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;
import br.com.f2e.starkbankwebhook.transfer.domain.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProcessCreditedInvoiceTest {

  @Mock private TransferIssuer transferIssuer;
  @Mock private ProcessedInvoiceCreditStore processedInvoiceCreditStore;

  private ProcessCreditedInvoice processCreditedInvoice;

  @BeforeEach
  void setUp() {
    processCreditedInvoice =
        new ProcessCreditedInvoice(transferIssuer, processedInvoiceCreditStore);
  }

  @Test
  void shouldIssueTransferAndMarkInvoiceCreditAsProcessed() {
    var creditedInvoice = new CreditedInvoice("invoice-id", 10_000, 500);

    processCreditedInvoice.execute(creditedInvoice);

    var order = inOrder(transferIssuer, processedInvoiceCreditStore);

    order.verify(transferIssuer).issue(new TransferRequest("invoice-id", 9_500));
    order.verify(processedInvoiceCreditStore).markAsProcessed(creditedInvoice.invoiceId());
  }

  @Test
  void shouldIgnoreAlreadyProcessedInvoiceCredit() {

    var creditedInvoice = new CreditedInvoice("invoice-id", 10_000, 500);

    when(processedInvoiceCreditStore.wasProcessed(creditedInvoice.invoiceId()))
        .thenReturn(Boolean.TRUE);
    processCreditedInvoice.execute(creditedInvoice);

    verify(transferIssuer, never()).issue(new TransferRequest("invoice-id", 9_500));
    verify(processedInvoiceCreditStore, never()).markAsProcessed(creditedInvoice.invoiceId());
  }

  @Test
  void shouldNotMarkInvoiceCreditAsProcessedWhenTransferFails() {
    var creditedInvoice = new CreditedInvoice("invoice-id", 10_000, 500);

    var transferRequest = new TransferRequest("invoice-id", 9_500);

    var exception =
        new TransferIssuanceException("Failed to issue transfer", new RuntimeException());

    doThrow(exception).when(transferIssuer).issue(transferRequest);

    assertThatThrownBy(() -> processCreditedInvoice.execute(creditedInvoice))
        .isInstanceOf(TransferIssuanceException.class);

    verify(processedInvoiceCreditStore, never()).markAsProcessed(creditedInvoice.invoiceId());
  }
}
