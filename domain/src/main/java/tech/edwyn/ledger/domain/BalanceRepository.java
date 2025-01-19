package tech.edwyn.ledger.domain;

import org.iban4j.Iban;
import tech.edwyn.ledger.ports.BalanceStore;

import java.util.Optional;

public class BalanceRepository {
  private final BalanceStore balanceStore;
  
  public BalanceRepository(BalanceStore balanceStore) {
    this.balanceStore = balanceStore;
  }
  
  public Optional<Balance> findByIban(Iban iban) {
    return balanceStore.findByIban(iban);
  }
}
