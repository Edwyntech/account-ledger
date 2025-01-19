package tech.edwyn.ledger.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ledger")
public record LedgerProperties(Topics topics, Stores stores) {
  public record Topics(String accountCreated, String movementApplied, String balanceUpdated) {
  }
  
  public record Stores(String accountCreated, String movementsList, String balanceUpdated) {
  }
}
