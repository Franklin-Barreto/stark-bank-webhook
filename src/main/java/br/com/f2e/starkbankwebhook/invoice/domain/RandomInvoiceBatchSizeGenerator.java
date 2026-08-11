package br.com.f2e.starkbankwebhook.invoice.domain;

import java.util.Objects;
import java.util.random.RandomGenerator;

public final class RandomInvoiceBatchSizeGenerator {

  static final int MINIMUM_BATCH_SIZE = 8;
  static final int MAXIMUM_BATCH_SIZE = 12;

  private final RandomGenerator randomGenerator;

  public RandomInvoiceBatchSizeGenerator(RandomGenerator randomGenerator) {
    this.randomGenerator = Objects.requireNonNull(randomGenerator);
  }

  public int generate() {
    return randomGenerator.nextInt(MINIMUM_BATCH_SIZE, MAXIMUM_BATCH_SIZE + 1);
  }
}
