package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import br.com.f2e.starkbankwebhook.transfer.application.TransferIssuanceException;
import com.starkbank.Project;
import com.starkbank.Transfer;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SdkStarkBankTransferGatewayTest {

  private Project project;
  private SdkStarkBankTransferGateway gateway;

  @BeforeEach
  void setUp() {
    project = mock(Project.class);
    gateway = new SdkStarkBankTransferGateway(project);
  }

  @Test
  void shouldCreateTransfersThroughStarkBank() {
    var transfer = new Transfer();
    var transfers = List.of(transfer);

    try (var mockedTransfer = mockStatic(Transfer.class)) {
      mockedTransfer.when(() -> Transfer.create(transfers, project)).thenReturn(transfers);

      gateway.create(transfers);

      mockedTransfer.verify(() -> Transfer.create(transfers, project));
    }
  }

  @Test
  void shouldTranslateStarkBankFailure() {
    var transfers = List.of(new Transfer());
    var cause = new Exception("Stark Bank unavailable");

    try (var mockedTransfer = mockStatic(Transfer.class)) {
      mockedTransfer.when(() -> Transfer.create(transfers, project)).thenThrow(cause);

      assertThatThrownBy(() -> gateway.create(transfers))
          .isInstanceOf(TransferIssuanceException.class)
          .hasMessage("Failed to issue transfers through Stark Bank")
          .hasCause(cause);
    }
  }
}
