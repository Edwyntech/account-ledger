package tech.edwyn.ledger.documentation.config;

import io.cucumber.core.exception.CucumberException;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.test.TestRecord;
import org.iban4j.Iban;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.ResultActions;
import tech.edwyn.ledger.avro.account.AccountCreated;
import tech.edwyn.ledger.avro.account.MovementApplied;
import tech.edwyn.ledger.domain.Account;
import tech.edwyn.ledger.domain.Customer;
import tech.edwyn.ledger.domain.Movement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class World {
  private static final Logger                                log = LoggerFactory.getLogger(World.class);
  private final        TestInputTopic<Iban, AccountCreated>  accountCreatedTopic;
  private final        TestInputTopic<Iban, MovementApplied> movementAppliedTopic;
  
  public List<Customer> customers = new ArrayList<>();
  public ResultActions  resultActions;
  
  public World(
    TestInputTopic<Iban, AccountCreated> accountCreatedTopic,
    TestInputTopic<Iban, MovementApplied> movementAppliedTopic) {
    log.trace("init");
    
    this.accountCreatedTopic = accountCreatedTopic;
    this.movementAppliedTopic = movementAppliedTopic;
  }
  
  @NotNull
  private static Function<AccountCreated, TestRecord<Iban, AccountCreated>> mapAccountCreatedToTestRecord() {
    log.trace("mapAccountCreatedToTestRecord");
    
    return accountCreated -> new TestRecord<>(accountCreated.getIban(), accountCreated);
  }
  
  @NotNull
  private static Function<Account, AccountCreated> mapAccountToAccountCreated() {
    log.trace("mapAccountToAccountCreated");
    
    return account -> AccountCreated.newBuilder()
                                    .setIban(account.iban())
                                    .setCustomerId(account.customerId())
                                    .setTimestamp(account.createdAt())
                                    .setLabel(account.label())
                                    .build();
  }
  
  @NotNull
  private static Function<MovementApplied, TestRecord<Iban, MovementApplied>> mapMovementAppliedToTestRecord() {
    log.trace("mapMovementAppliedToTestRecord");
    
    return movementApplied -> new TestRecord<>(movementApplied.getIban(), movementApplied);
  }
  
  @NotNull
  private static Function<Movement, MovementApplied> mapMovementToMovementApplied() {
    log.trace("mapMovementToMovementApplied");
    
    return movement -> MovementApplied.newBuilder()
                                      .setIban(movement.iban())
                                      .setAmount(movement.amount())
                                      .setTimestamp(movement.timestamp())
                                      .build();
  }
  
  public Customer customerWithFirstName(String firstName) {
    log.trace("customerWithFirstName - firstName: {}", firstName);
    
    return customers.stream()
                    .filter(c -> c.firstName()
                                  .equals(firstName))
                    .findFirst()
                    .orElseThrow(() -> new CucumberException("Customer not found with first name: " + firstName));
  }
  
  public void create(List<Account> accounts) {
    log.trace("create - accounts: {}", accounts);
    
    accounts.stream()
            .map(mapAccountToAccountCreated())
            .map(mapAccountCreatedToTestRecord())
            .forEach(accountCreatedTopic::pipeInput);
  }
  
  public void observe(List<Movement> movements) {
    log.trace("observe - movements: {}", movements);
    
    movements.stream()
             .map(mapMovementToMovementApplied())
             .map(mapMovementAppliedToTestRecord())
             .forEach(movementAppliedTopic::pipeInput);
  }
  
}
