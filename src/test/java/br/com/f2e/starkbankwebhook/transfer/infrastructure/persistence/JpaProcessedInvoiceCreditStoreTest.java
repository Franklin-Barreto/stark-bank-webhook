package br.com.f2e.starkbankwebhook.transfer.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.f2e.starkbankwebhook.shared.infrastructure.persistence.test.PostgresTestContainerConfiguration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({PostgresTestContainerConfiguration.class, JpaProcessedInvoiceCreditStore.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class JpaProcessedInvoiceCreditStoreTest {

  @Autowired private JpaProcessedInvoiceCreditStore store;

  @Test
  void shouldAllowOnlyOneConcurrentClaimForTheSameInvoiceCredit() throws Exception {
    var invoiceId = "concurrent-invoice-id";
    var start = new CountDownLatch(1);

    try (var executor = Executors.newFixedThreadPool(2)) {
      var first = executor.submit(() -> claimAfter(start, invoiceId));
      var second = executor.submit(() -> claimAfter(start, invoiceId));

      start.countDown();

      assertThat(java.util.List.of(first.get(), second.get()))
          .containsExactlyInAnyOrder(true, false);
    } finally {
      store.releaseClaim(invoiceId);
    }
  }

  private boolean claimAfter(CountDownLatch start, String invoiceId) throws InterruptedException {
    start.await();
    return store.tryClaim(invoiceId);
  }

  @Test
  void shouldClaimInvoiceCreditOnlyOnceAndReleaseIt() {
    var invoiceId = "invoice-id";

    assertThat(store.tryClaim(invoiceId)).isTrue();
    assertThat(store.tryClaim(invoiceId)).isFalse();

    store.releaseClaim(invoiceId);

    assertThat(store.tryClaim(invoiceId)).isTrue();
  }
}
