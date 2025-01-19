package tech.edwyn.ledger.domain;

import org.iban4j.Iban;
import tech.edwyn.ledger.ports.AccountStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountRepository {
  
  private final AccountStore accountStore;
  
  public AccountRepository(AccountStore accountStore) {
    this.accountStore = accountStore;
  }
  
  public List<Account> findAllByCustomerId(UUID customerId) {
    return accountStore.findAllByCustomerId(customerId);
  }
  
  public Optional<Account> findByIban(Iban iban) {
    return accountStore.findByIban(iban);
  }
}
