package tech.edwyn.ledger.ports;

import org.iban4j.Iban;
import tech.edwyn.ledger.domain.Balance;

import java.util.Optional;

public interface BalanceStore {
  Optional<Balance> findByIban(Iban iban);
}
