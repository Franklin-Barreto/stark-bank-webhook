package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.f2e.starkbankwebhook.transfer.domain.TransferRequest;
import org.junit.jupiter.api.Test;

class StarkBankTransferMapperTest {

  private final StarkBankTransferMapper mapper = new StarkBankTransferMapper();

  @Test
  void shouldMapTransferRequestToStarkBankTransfer() {
    var request = new TransferRequest("invoice-id", 9_500);

    var transfer = mapper.map(request);

    assertThat(transfer.amount).isEqualTo(9_500);
    assertThat(transfer.externalId).isEqualTo("invoice-id");
    assertThat(transfer.bankCode).isEqualTo("20018183");
    assertThat(transfer.branchCode).isEqualTo("0001");
    assertThat(transfer.accountNumber).isEqualTo("6341320293482496");
    assertThat(transfer.name).isEqualTo("Stark Bank S.A.");
    assertThat(transfer.taxId).isEqualTo("20.018.183/0001-80");
    assertThat(transfer.accountType).isEqualTo("payment");
    assertThat(transfer.tags).containsExactly("credited-invoice", "invoice/invoice-id");
    assertThat(transfer.rules)
        .singleElement()
        .satisfies(
            rule -> {
              assertThat(rule.key).isEqualTo("resendingLimit");
              assertThat(rule.value).isEqualTo(5);
            });
  }
}
