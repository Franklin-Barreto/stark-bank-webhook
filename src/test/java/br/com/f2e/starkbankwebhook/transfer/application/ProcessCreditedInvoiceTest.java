package br.com.f2e.starkbankwebhook.transfer.application;

import static org.mockito.Mockito.verify;

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

  private ProcessCreditedInvoice processCreditedInvoice;

  @BeforeEach
  void setUp() {
    processCreditedInvoice = new ProcessCreditedInvoice(transferIssuer);
  }

  @Test
  void shouldIssueTransferWithCreditedInvoiceNetAmount() {
    var creditedInvoice = new CreditedInvoice("invoice-id", 10_000, 500);

    processCreditedInvoice.execute(creditedInvoice);

    verify(transferIssuer).issue(new TransferRequest("invoice-id", 9_500));
  }
}
