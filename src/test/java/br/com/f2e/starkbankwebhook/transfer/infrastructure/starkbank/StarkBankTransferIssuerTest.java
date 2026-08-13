package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.f2e.starkbankwebhook.transfer.domain.TransferRequest;
import com.starkbank.Transfer;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StarkBankTransferIssuerTest {

  @Mock private StarkBankTransferMapper mapper;
  @Mock private StarkBankTransferGateway gateway;

  private StarkBankTransferIssuer starkBankTransferIssuer;

  @BeforeEach
  void setUp() {
    starkBankTransferIssuer = new StarkBankTransferIssuer(mapper, gateway);
  }

  @Test
  void shouldMapAndCreateTransfer() {
    var request = new TransferRequest("invoice-id", 9_500);
    var transfer = new Transfer();

    when(mapper.map(request)).thenReturn(transfer);

    starkBankTransferIssuer.issue(request);

    verify(gateway).create(List.of(transfer));
  }
}
