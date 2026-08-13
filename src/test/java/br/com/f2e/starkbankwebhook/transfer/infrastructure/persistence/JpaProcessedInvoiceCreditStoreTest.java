package br.com.f2e.starkbankwebhook.transfer.infrastructure.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import br.com.f2e.starkbankwebhook.shared.infrastructure.persistence.test.PostgresTestContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({PostgresTestContainerConfiguration.class, JpaProcessedInvoiceCreditStore.class})
class JpaProcessedInvoiceCreditStoreTest {

  @Autowired private JpaProcessedInvoiceCreditStore store;

  @Test
  void shouldPersistAndFindProcessedInvoiceCredit() {
    var invoiceId = "invoice-id";

    assertThat(store.wasProcessed(invoiceId)).isFalse();

    store.markAsProcessed(invoiceId);

    assertThat(store.wasProcessed(invoiceId)).isTrue();
  }
}
