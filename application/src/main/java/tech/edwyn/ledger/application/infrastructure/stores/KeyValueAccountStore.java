package tech.edwyn.ledger.application.infrastructure.stores;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.iban4j.Iban;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.ledger.avro.account.AccountCreated;
import tech.edwyn.ledger.domain.Account;
import tech.edwyn.ledger.ports.AccountStore;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class KeyValueAccountStore implements AccountStore {
  private static final Logger log = LoggerFactory.getLogger(KeyValueAccountStore.class);
  
  private final Supplier<ReadOnlyKeyValueStore<Iban, AccountCreated>> accountCreatedStore;
  
  public KeyValueAccountStore(Supplier<ReadOnlyKeyValueStore<Iban, AccountCreated>> accountCreatedStore) {
    log.trace("init - accountCreatedStore: {}", accountCreatedStore);
    
    this.accountCreatedStore = accountCreatedStore;
  }
  
  @NotNull
  private static Function<AccountCreated, Account> mapAccountCreatedToAccount() {
    log.trace("mapAccountCreatedToAccount");
    
    return accountCreated -> new Account(
      accountCreated.getIban(),
      accountCreated.getCustomerId(),
      accountCreated.getLabel()
                    .toString(),
      accountCreated.getTimestamp()
    );
  }
  
  @Override
  public List<Account> findAllByCustomerId(UUID customerId) {
    log.trace("findAllByCustomerId - customerId: {}", customerId);
    
    Spliterator<KeyValue<Iban, AccountCreated>> spliterator =
      Spliterators.spliteratorUnknownSize(
        accountCreatedStore.get()
                           .all(),
        Spliterator.ORDERED);
    
    return StreamSupport.stream(spliterator, false)
                        .filter(kv -> kv.value.getCustomerId()
                                              .equals(customerId))
                        .map(kv -> kv.value)
                        .map(mapAccountCreatedToAccount())
                        .toList();
  }
  
  @Override
  public Optional<Account> findByIban(Iban iban) {
    log.trace("findByIban - iban: {}", iban);
    
    return Optional.ofNullable(accountCreatedStore.get()
                                                  .get(iban))
                   .map(mapAccountCreatedToAccount());
  }
}
