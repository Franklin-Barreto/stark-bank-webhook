package br.com.f2e.starkbankwebhook.transfer.infrastructure.webhook;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidWebhookException extends RuntimeException {
  InvalidWebhookException(Throwable cause) {
    super("Invalid Stark Bank webhook", cause);
  }
}
