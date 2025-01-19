package tech.edwyn.ledger.application.infrastructure.stores;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.ledger.avro.account.BalanceUpdated;
import tech.edwyn.ledger.domain.Balance;
import tech.edwyn.ledger.ports.BalanceStore;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyValueBalanceStore implements BalanceStore {
  private static final Logger log = LoggerFactory.getLogger(KeyValueBalanceStore.class);
  
  private final Supplier<ReadOnlyKeyValueStore<Iban, BalanceUpdated>> balanceUpdatedStore;
  
  public KeyValueBalanceStore(Supplier<ReadOnlyKeyValueStore<Iban, BalanceUpdated>> balanceUpdatedStore) {
    log.trace("init - balanceUpdatedStore: {}", balanceUpdatedStore);
    
    this.balanceUpdatedStore = balanceUpdatedStore;
  }
  
  @Override
  public Optional<Balance> findByIban(Iban iban) {
    log.trace("findByIban - iban: {}", iban);
    
    return Optional.ofNullable(balanceUpdatedStore.get()
                                                  .get(iban))
                   .map(mapBalanceUpdatedToBalance());
  }
  
  private Function<BalanceUpdated, Balance> mapBalanceUpdatedToBalance() {
    log.trace("mapBalanceUpdatedToBalance");
    
    return balanceUpdated -> new Balance(
      balanceUpdated.getIban(),
      balanceUpdated.getAmount(),
      balanceUpdated.getTimestamp());
  }
}
