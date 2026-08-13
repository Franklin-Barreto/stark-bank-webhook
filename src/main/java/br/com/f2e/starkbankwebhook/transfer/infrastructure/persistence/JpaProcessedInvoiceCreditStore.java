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
  public boolean wasProcessed(String invoiceId) {
    return repository.existsById(invoiceId);
  }

  @Override
  public void markAsProcessed(String invoiceId) {
    repository.save(new ProcessedInvoiceCreditEntity(invoiceId, Instant.now()));
  }
}
