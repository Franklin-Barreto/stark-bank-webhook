package br.com.f2e.starkbankwebhook.invoice.infrastructure.scheduling;

import br.com.f2e.starkbankwebhook.invoice.application.IssueInvoiceBatch;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "starkbank.invoice", name = "run-on-startup", havingValue = "true")
public class IssueInvoiceBatchOnStartup implements ApplicationRunner {

  private final IssueInvoiceBatch issueInvoiceBatch;

  public IssueInvoiceBatchOnStartup(IssueInvoiceBatch issueInvoiceBatch) {
    this.issueInvoiceBatch = Objects.requireNonNull(issueInvoiceBatch);
  }

  @Override
  public void run(@NonNull ApplicationArguments ignored) {
    issueInvoiceBatch.execute();
  }
}
