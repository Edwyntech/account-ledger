package tech.edwyn.ledger.ports;

import org.iban4j.Iban;
import tech.edwyn.ledger.domain.Movement;

import java.util.List;

public interface MovementStore {
  List<Movement> findAllByIban(Iban iban);
}
