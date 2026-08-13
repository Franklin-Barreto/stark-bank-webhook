package br.com.f2e.starkbankwebhook.transfer.infrastructure.persistence;

import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

interface ProcessedInvoiceCreditRepository
    extends JpaRepository<ProcessedInvoiceCreditEntity, String> {

  @Modifying
  @Transactional
  @Query(
      value =
          """
          INSERT INTO processed_invoice_credit (invoice_id, processed_at)
          VALUES (:invoiceId, :processedAt)
          ON CONFLICT (invoice_id) DO NOTHING
          """,
      nativeQuery = true)
  int insertIfAbsent(
      @Param("invoiceId") String invoiceId, @Param("processedAt") Instant processedAt);
}
