package br.com.f2e.starkbankwebhook.invoice.domain;

import java.math.BigDecimal;

public final class Preconditions {

  private static final String GREATER_THAN_ZERO = "%s must be greater than zero";

  private Preconditions() {}

  public static <T> void requireNotNull(T field, String fieldName) {
    if (field == null) {
      throw new IllegalArgumentException("%s must not be null".formatted(fieldName));
    }
  }

  public static String requireNotBlank(String field, String fieldName) {
    requireNotNull(field, fieldName);

    var trimmed = field.trim();

    if (trimmed.isBlank()) {
      throw new IllegalArgumentException("%s must not be blank".formatted(fieldName));
    }

    return trimmed;
  }

  public static <T extends Number> T requirePositive(T value, String fieldName) {
    requireNotNull(value, fieldName);

    var numericValue = toBigDecimal(value, fieldName);

    if (numericValue.signum() <= 0) {
      throw new IllegalArgumentException(GREATER_THAN_ZERO.formatted(fieldName));
    }

    return value;
  }


  private static BigDecimal toBigDecimal(Number value, String fieldName) {
    try {
      return new BigDecimal(value.toString());
    } catch (NumberFormatException exception) {
      throw new IllegalArgumentException(
          "%s must be a finite number".formatted(fieldName), exception);
    }
  }
}
