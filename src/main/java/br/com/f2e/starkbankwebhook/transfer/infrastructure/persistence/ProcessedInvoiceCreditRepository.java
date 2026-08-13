package br.com.f2e.starkbankwebhook.transfer.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProcessedInvoiceCreditRepository
    extends JpaRepository<ProcessedInvoiceCreditEntity, String> {}
