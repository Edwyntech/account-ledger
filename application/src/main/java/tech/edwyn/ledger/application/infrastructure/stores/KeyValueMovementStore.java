package tech.edwyn.ledger.application.infrastructure.stores;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.ledger.avro.account.MovementApplied;
import tech.edwyn.ledger.avro.account.MovementsList;
import tech.edwyn.ledger.domain.Movement;
import tech.edwyn.ledger.ports.MovementStore;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyValueMovementStore implements MovementStore {
  private static final Logger log = LoggerFactory.getLogger(KeyValueMovementStore.class);
  
  private final Supplier<ReadOnlyKeyValueStore<Iban, MovementsList>> movementAppliedStore;
  
  public KeyValueMovementStore(Supplier<ReadOnlyKeyValueStore<Iban, MovementsList>> movementsListStore) {
    log.trace("init - movementsListStore: {}", movementsListStore);
    
    this.movementAppliedStore = movementsListStore;
  }
  
  private Function<MovementApplied, Movement> mapMovementAppliedToMovement() {
    log.trace("mapMovementAppliedToMovement");
    
    return movementApplied -> new Movement(
      movementApplied.getIban(),
      movementApplied.getAmount(),
      movementApplied.getTimestamp()
    );
  }
  
  @Override
  public List<Movement> findAllByIban(Iban iban) {
    log.trace("findAllByIban - iban: {}", iban);
    
    return Optional.ofNullable(movementAppliedStore.get()
                                                   .get(iban))
                   .stream()
                   .flatMap(movementsList -> movementsList.getMovementsApplied()
                                                          .stream())
                   .map(mapMovementAppliedToMovement())
                   .toList();
  }
}
