package tech.edwyn.ledger.application.streams;

import org.apache.kafka.streams.processor.api.ContextualFixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;
import org.iban4j.Iban;
import org.javamoney.moneta.Money;
import tech.edwyn.ledger.avro.account.AccountCreated;
import tech.edwyn.ledger.avro.account.BalanceUpdated;
import tech.edwyn.ledger.domain.Account;

public class InitializeAccountBalance extends ContextualFixedKeyProcessor<Iban, AccountCreated, BalanceUpdated> {
  
  private final String                              accountCreatedStoreName;
  private final String                              balanceUpdatedStoreName;
  private       KeyValueStore<Iban, AccountCreated> accountCreatedStore;
  private       KeyValueStore<Iban, BalanceUpdated> balanceUpdatedStore;
  
  public InitializeAccountBalance(String accountCreatedStoreName, String balanceUpdatedStoreName) {
    this.accountCreatedStoreName = accountCreatedStoreName;
    this.balanceUpdatedStoreName = balanceUpdatedStoreName;
  }
  
  @Override
  public void init(FixedKeyProcessorContext<Iban, BalanceUpdated> context) {
    super.init(context);
    accountCreatedStore = context.getStateStore(accountCreatedStoreName);
    balanceUpdatedStore = context.getStateStore(balanceUpdatedStoreName);
  }
  
  @Override
  public void process(FixedKeyRecord<Iban, AccountCreated> accountCreatedRecord) {
    Iban           iban           = accountCreatedRecord.key();
    AccountCreated accountCreated = accountCreatedRecord.value();
    accountCreatedStore.put(iban, accountCreated);
    
    BalanceUpdated balanceUpdated = BalanceUpdated.newBuilder()
                                                  .setTimestamp(accountCreated.getTimestamp())
                                                  .setIban(accountCreatedRecord.value()
                                                                               .getIban())
                                                  .setAmount(Money.zero(Account.currencyFrom(iban)))
                                                  .build();
    balanceUpdatedStore.put(iban, balanceUpdated);
    
    context().forward(accountCreatedRecord.withValue(balanceUpdated));
  }
}
