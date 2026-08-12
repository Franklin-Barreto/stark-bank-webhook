package br.com.f2e.starkbankwebhook.invoice.infrastructure.starkbank;

import com.starkbank.Invoice;
import java.util.List;

interface StarkBankInvoiceGateway {

  void create(List<Invoice> invoices);
}
