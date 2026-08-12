package br.com.f2e.starkbankwebhook.invoice.application;

import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class InvoiceBatchGenerator {

  private final RandomInvoiceBatchSizeGenerator batchSizeGenerator;
  private final RandomInvoiceDraftGenerator draftGenerator;

  public InvoiceBatchGenerator(
      RandomInvoiceBatchSizeGenerator batchSizeGenerator,
      RandomInvoiceDraftGenerator draftGenerator) {
    this.batchSizeGenerator = Objects.requireNonNull(batchSizeGenerator);
    this.draftGenerator = Objects.requireNonNull(draftGenerator);
  }

  public List<InvoiceDraft> generate() {

    var batchSize = batchSizeGenerator.generate();
    var drafts = new ArrayList<InvoiceDraft>(batchSize);

    for (int i = 0; i < batchSize; i++) {
      drafts.add(draftGenerator.generate());
    }
    return drafts;
  }
}
