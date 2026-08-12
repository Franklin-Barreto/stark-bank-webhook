package br.com.f2e.starkbankwebhook.transfer.infrastructure.webhook;

import br.com.f2e.starkbankwebhook.shared.infrastructure.starkbank.ConditionalOnStarkBankEnabled;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnStarkBankEnabled
@RestController
@RequestMapping("/webhooks/starkbank")
public class StarkBankWebhookController {

  private final StarkBankWebhookParser webhookParser;

  public StarkBankWebhookController(StarkBankWebhookParser webhookParser) {
    this.webhookParser = webhookParser;
  }

  @PostMapping
  public ResponseEntity<Void> handle(
      @RequestHeader("Digital-signature") String signature, @RequestBody String payload) {

    webhookParser.parse(payload, signature);
    return ResponseEntity.ok().build();
  }
}
