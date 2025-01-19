package tech.edwyn.ledger.application.streams;

import org.apache.kafka.streams.processor.api.ContextualFixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;
import org.iban4j.Iban;
import org.javamoney.moneta.Money;
import tech.edwyn.ledger.avro.account.BalanceUpdated;
import tech.edwyn.ledger.avro.account.MovementApplied;
import tech.edwyn.ledger.avro.account.MovementsList;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.util.ArrayList;
import java.util.Optional;

public class UpdateAccountBalance extends ContextualFixedKeyProcessor<Iban, MovementApplied, BalanceUpdated> {
  
  private final String                              movementsListStoreName;
  private final String                              balanceUpdatedStoreName;
  private       KeyValueStore<Iban, MovementsList>  movementsListStore;
  private       KeyValueStore<Iban, BalanceUpdated> balanceUpdatedStore;
  
  public UpdateAccountBalance(String movementsListStoreName, String balanceUpdatedStoreName) {
    this.movementsListStoreName = movementsListStoreName;
    this.balanceUpdatedStoreName = balanceUpdatedStoreName;
  }
  
  @Override
  public void init(FixedKeyProcessorContext<Iban, BalanceUpdated> context) {
    super.init(context);
    movementsListStore = context.getStateStore(movementsListStoreName);
    balanceUpdatedStore = context.getStateStore(balanceUpdatedStoreName);
  }
  
  @Override
  public void process(FixedKeyRecord<Iban, MovementApplied> movementAppliedRecord) {
    Iban            iban            = movementAppliedRecord.key();
    MovementApplied movementApplied = movementAppliedRecord.value();
    
    BalanceUpdated currentBalanceUpdated = balanceUpdatedStore.get(iban);
    MovementsList movementsList = Optional.ofNullable(movementsListStore.get(iban))
                                          .orElseGet(() -> MovementsList.newBuilder()
                                                                        .setMovementsApplied(new ArrayList<>())
                                                                        .build());
    
    Money updatedAmount = updateAmount(currentBalanceUpdated.getAmount(), movementApplied.getAmount());
    if (updatedAmount.isNegative()) {
      throw new OverdraftForbidden();
    }
    
    BalanceUpdated newBalanceUpdated = BalanceUpdated.newBuilder(currentBalanceUpdated)
                                                     .setAmount(updatedAmount)
                                                     .setTimestamp(movementApplied.getTimestamp())
                                                     .build();
    
    movementsList.getMovementsApplied()
                 .add(movementApplied);
    movementsListStore.put(iban, movementsList);
    balanceUpdatedStore.put(iban, newBalanceUpdated);
    
    context().forward(movementAppliedRecord.withValue(newBalanceUpdated));
  }
  
  private Money updateAmount(Money balanceAmount, Money movementAmount) {
    CurrencyConversion conversion              = MonetaryConversions.getConversion(balanceAmount.getCurrency());
    Money              convertedMovementAmount = movementAmount.with(conversion);
    
    return balanceAmount.add(convertedMovementAmount);
  }
  
}
