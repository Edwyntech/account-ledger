package tech.edwyn.ledger.domain;

import org.iban4j.Iban;
import tech.edwyn.ledger.ports.MovementStore;

import java.util.List;

public class MovementRepository {
  private final MovementStore movementStore;
  
  public MovementRepository(MovementStore movementStore) {
    this.movementStore = movementStore;
  }
  
  public List<Movement> findAllByIban(Iban iban) {
    return movementStore.findAllByIban(iban);
  }
}
