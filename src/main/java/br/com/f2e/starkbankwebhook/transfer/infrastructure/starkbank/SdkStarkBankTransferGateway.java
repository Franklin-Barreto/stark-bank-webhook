package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.transfer.application.TransferIssuanceException;
import com.starkbank.Project;
import com.starkbank.Transfer;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SdkStarkBankTransferGateway implements StarkBankTransferGateway {

  private static final Logger LOGGER = LoggerFactory.getLogger(SdkStarkBankTransferGateway.class);

  private final Project project;

  SdkStarkBankTransferGateway(Project project) {
    this.project = Objects.requireNonNull(project);
  }

  @Override
  public void create(List<Transfer> transfers) {
    try {
      LOGGER.info("Issuing transfer batch: size={}", transfers.size());

      var createdTransfers = Transfer.create(transfers, project);

      LOGGER.info(
          "Transfer batch issued successfully: requested={}, created={}",
          transfers.size(),
          createdTransfers.size());

      createdTransfers.forEach(
          transfer ->
              LOGGER.info(
                  "Transfer issued: transferId={}, externalId={}, status={}",
                  transfer.id,
                  transfer.externalId,
                  transfer.status));
    } catch (Exception exception) {
      throw new TransferIssuanceException(
          "Failed to issue transfers through Stark Bank", exception);
    }
  }
}
