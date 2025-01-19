package tech.edwyn.ledger.application.streams.serde;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.iban4j.Iban;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import tech.edwyn.ledger.avro.account.AccountCreated;
import tech.edwyn.ledger.avro.account.BalanceUpdated;
import tech.edwyn.ledger.avro.account.MovementApplied;
import tech.edwyn.ledger.avro.account.MovementsList;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LedgerSerdes {
  private final Map<String, Object> serdeConfigurationMap;
  
  public LedgerSerdes(KafkaStreamsConfiguration kafkaStreamsConfiguration) {
    this.serdeConfigurationMap = propertiesToMap(kafkaStreamsConfiguration.asProperties());
  }
  
  public Serde<Iban> iban() {
    Serde<Iban> ibanSerde = new IbanSerde();
    ibanSerde.configure(serdeConfigurationMap, true);
    return ibanSerde;
  }
  
  public Serde<AccountCreated> accountCreated() {
    Serde<AccountCreated> accountCreatedSerde = new SpecificAvroSerde<>();
    accountCreatedSerde.configure(serdeConfigurationMap, false);
    return accountCreatedSerde;
  }
  
  public Serde<MovementApplied> movementApplied() {
    Serde<MovementApplied> movementGeneratedSerde = new SpecificAvroSerde<>();
    movementGeneratedSerde.configure(serdeConfigurationMap, false);
    return movementGeneratedSerde;
  }
  
  public Serde<MovementsList> movementsList() {
    Serde<MovementsList> movementsListSerde = new SpecificAvroSerde<>();
    movementsListSerde.configure(serdeConfigurationMap, false);
    return movementsListSerde;
  }
  
  public Serde<BalanceUpdated> balanceUpdated() {
    Serde<BalanceUpdated> balanceUpdatedSerde = new SpecificAvroSerde<>();
    balanceUpdatedSerde.configure(serdeConfigurationMap, false);
    return balanceUpdatedSerde;
  }
  
  private Map<String, Object> propertiesToMap(final Properties properties) {
    final Map<String, Object> configs = new HashMap<>();
    properties.forEach((key, value) -> configs.put((String) key, value));
    return configs;
  }
}
