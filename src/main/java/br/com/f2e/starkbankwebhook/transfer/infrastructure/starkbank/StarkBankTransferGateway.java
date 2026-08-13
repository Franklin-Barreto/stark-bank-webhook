package br.com.f2e.starkbankwebhook.transfer.infrastructure.starkbank;

import com.starkbank.Transfer;
import java.util.List;

public interface StarkBankTransferGateway {

  void create(List<Transfer> transfers);
}
