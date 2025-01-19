package tech.edwyn.ledger.ports;

import org.iban4j.Iban;
import tech.edwyn.ledger.domain.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountStore {
  List<Account> findAllByCustomerId(UUID customerId);
  
  Optional<Account> findByIban(Iban iban);
}
