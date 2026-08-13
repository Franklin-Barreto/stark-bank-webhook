package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import br.com.f2e.starkbankwebhook.transfer.domain.TransferRequest;
import com.starkbank.Transfer;
import java.util.List;
import java.util.Map;

public final class StarkBankTransferMapper {

  private static final String BANK_CODE = "bankCode";
  private static final String BANK_CODE_VALUE = "20018183";
  private static final String BRANCH_CODE = "branchCode";
  private static final String BRANCH_CODE_VALUE = "0001";
  private static final String ACCOUNT_NUMBER = "accountNumber";
  private static final String ACCOUNT_NUMBER_VALUE = "6341320293482496";
  private static final String NAME = "name";
  private static final String NAME_VALUE = "Stark Bank S.A.";
  private static final String TAX_ID = "taxId";
  private static final String TAX_ID_VALUE = "20.018.183/0001-80";
  private static final String ACCOUNT_TYPE = "accountType";
  private static final String ACCOUNT_TYPE_VALUE = "payment";
  private static final String EXTERNAL_ID = "externalId";
  private static final String AMOUNT = "amount";
  private static final String TAGS = "tags";
  private static final String RULES = "rules";
  private static final String CREDITED_INVOICE_TAG = "credited-invoice";
  private static final String INVOICE_TAG_PREFIX = "invoice/";
  private static final String RESENDING_LIMIT = "resendingLimit";
  private static final int RESENDING_LIMIT_VALUE = 5;

  public Transfer map(TransferRequest request) {
    var transfer =
        Map.<String, Object>of(
            AMOUNT,
            request.amount(),
            BANK_CODE,
            BANK_CODE_VALUE,
            BRANCH_CODE,
            BRANCH_CODE_VALUE,
            ACCOUNT_NUMBER,
            ACCOUNT_NUMBER_VALUE,
            NAME,
            NAME_VALUE,
            TAX_ID,
            TAX_ID_VALUE,
            ACCOUNT_TYPE,
            ACCOUNT_TYPE_VALUE,
            EXTERNAL_ID,
            request.externalId(),
            TAGS,
            new String[] {CREDITED_INVOICE_TAG, INVOICE_TAG_PREFIX + request.externalId()},
            RULES,
            List.of(new Transfer.Rule(RESENDING_LIMIT, RESENDING_LIMIT_VALUE)));
    try {
      return new Transfer(transfer);

    } catch (Exception exception) {
      throw new IllegalStateException(
          "Failed to map transfer request to Stark Bank transfer", exception);
    }
  }
}
