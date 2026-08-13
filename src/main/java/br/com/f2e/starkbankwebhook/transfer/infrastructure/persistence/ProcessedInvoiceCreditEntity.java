package br.com.f2e.starkbankwebhook.transfer.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "processed_invoice_credit")
class ProcessedInvoiceCreditEntity {

  @Id
  @Column(name = "invoice_id", nullable = false)
  private String invoiceId;

  @Column(name = "processed_at", nullable = false)
  private Instant processedAt;

  protected ProcessedInvoiceCreditEntity() {}

  ProcessedInvoiceCreditEntity(String invoiceId, Instant processedAt) {
    this.invoiceId = invoiceId;
    this.processedAt = processedAt;
  }
}
