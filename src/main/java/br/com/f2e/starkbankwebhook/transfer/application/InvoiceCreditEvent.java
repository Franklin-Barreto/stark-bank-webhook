package br.com.f2e.starkbankwebhook.transfer.application;

import br.com.f2e.starkbankwebhook.transfer.domain.CreditedInvoice;

public record InvoiceCreditEvent(String eventId, CreditedInvoice creditedInvoice) {}
