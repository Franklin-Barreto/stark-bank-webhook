package br.com.f2e.starkbankwebhook.invoice.application;

import br.com.f2e.starkbankwebhook.invoice.domain.InvoiceDraft;
import br.com.f2e.starkbankwebhook.invoice.domain.Payer;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public final class RandomInvoiceDraftGenerator {

  private static final long MINIMUM_AMOUNT_IN_REALS = 10;
  private static final long MAXIMUM_AMOUNT_IN_REALS = 100;
  private static final long CENTS_PER_REAL = 100;

  private static final List<Payer> PAYERS =
      List.of(
          new Payer("Jon Snow", "123.456.789-09"),
          new Payer("Arya Stark", "987.654.321-00"),
          new Payer("Sansa Stark", "529.982.247-25"),
          new Payer("Bran Stark", "111.444.777-35"),
          new Payer("Robb Stark", "390.533.447-05"),
          new Payer("Ned Stark", "168.995.350-09"),
          new Payer("Catelyn Stark", "862.883.667-57"),
          new Payer("Tyrion Lannister", "153.509.460-56"),
          new Payer("Jaime Lannister", "714.287.938-60"),
          new Payer("Daenerys Targaryen", "418.674.598-68"));

  private final RandomGenerator randomGenerator;

  public RandomInvoiceDraftGenerator(RandomGenerator randomGenerator) {
    this.randomGenerator = Objects.requireNonNull(randomGenerator);
  }

  public InvoiceDraft generate() {
    var payerIndex = randomGenerator.nextInt(PAYERS.size());
    var amountInReals =
        randomGenerator.nextLong(MINIMUM_AMOUNT_IN_REALS, MAXIMUM_AMOUNT_IN_REALS + 1);
    var amountInCents = amountInReals * CENTS_PER_REAL;
    var payer = PAYERS.get(payerIndex);

    return new InvoiceDraft(amountInCents, payer.name(), payer.taxId());
  }
}
