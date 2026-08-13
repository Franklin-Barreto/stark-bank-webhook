package br.com.f2e.starkbankwebhook.transfer.infrastructure.persistence;

import br.com.f2e.starkbankwebhook.transfer.application.ProcessedInvoiceCreditStore;
import java.time.Instant;
import java.util.Objects;
import org.springframework.stereotype.Repository;

@Repository
class JpaProcessedInvoiceCreditStore implements ProcessedInvoiceCreditStore {

  private final ProcessedInvoiceCreditRepository repository;

  public JpaProcessedInvoiceCreditStore(ProcessedInvoiceCreditRepository repository) {
    this.repository = Objects.requireNonNull(repository);
  }

  @Override
  public boolean tryClaim(String invoiceId) {
    return repository.insertIfAbsent(invoiceId, Instant.now()) == 1;
  }

  @Override
  public void releaseClaim(String invoiceId) {
    repository.deleteById(invoiceId);
  }
}
