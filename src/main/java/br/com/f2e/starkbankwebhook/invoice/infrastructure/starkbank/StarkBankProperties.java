package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "starkbank")
public record StarkBankProperties(
    boolean enabled, String environment, String projectId, String privateKey) {}
