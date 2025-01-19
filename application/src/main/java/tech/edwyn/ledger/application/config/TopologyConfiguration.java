package tech.edwyn.ledger.application.config;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorSupplier;
import org.apache.kafka.streams.state.Stores;
import org.iban4j.Iban;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import tech.edwyn.ledger.application.LedgerProperties;
import tech.edwyn.ledger.application.streams.InitializeAccountBalance;
import tech.edwyn.ledger.application.streams.UpdateAccountBalance;
import tech.edwyn.ledger.application.streams.serde.LedgerSerdes;
import tech.edwyn.ledger.avro.account.AccountCreated;
import tech.edwyn.ledger.avro.account.BalanceUpdated;
import tech.edwyn.ledger.avro.account.MovementApplied;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class TopologyConfiguration {
  private final LedgerProperties.Topics topics;
  private final LedgerProperties.Stores stores;
  private final StreamsBuilder          streamsBuilder;
  private final LedgerSerdes            ledgerSerdes;
  
  public TopologyConfiguration(LedgerProperties ledgerProperties,
                               KafkaStreamsConfiguration kafkaStreamsConfiguration,
                               StreamsBuilder streamsBuilder) {
    this.topics = ledgerProperties.topics();
    this.stores = ledgerProperties.stores();
    this.streamsBuilder = streamsBuilder;
    this.ledgerSerdes = new LedgerSerdes(kafkaStreamsConfiguration);
  }
  
  @PostConstruct
  public void createStores() {
    streamsBuilder
      .addStateStore(
        Stores.keyValueStoreBuilder(
          Stores.persistentKeyValueStore(stores.accountCreated()),
          ledgerSerdes.iban(),
          ledgerSerdes.accountCreated()))
      .addStateStore(
        Stores.keyValueStoreBuilder(
          Stores.persistentKeyValueStore(stores.movementsList()),
          ledgerSerdes.iban(),
          ledgerSerdes.movementsList()))
      .addStateStore(
        Stores.keyValueStoreBuilder(
          Stores.persistentKeyValueStore(stores.balanceUpdated()),
          ledgerSerdes.iban(),
          ledgerSerdes.balanceUpdated())
      );
  }
  
  @Bean
  public LedgerSerdes ledgerSerdes() {
    return ledgerSerdes;
  }
  
  @Bean
  public FixedKeyProcessorSupplier<Iban, AccountCreated, BalanceUpdated> initializeAccountBalance() {
    return () -> new InitializeAccountBalance(stores.accountCreated(), stores.balanceUpdated());
  }
  
  @Bean
  public FixedKeyProcessorSupplier<Iban, MovementApplied, BalanceUpdated> updateAccountBalance() {
    return () -> new UpdateAccountBalance(stores.movementsList(), stores.balanceUpdated());
  }
  
  @Bean
  public KStream<Iban, BalanceUpdated> initializeBalanceStream(
    FixedKeyProcessorSupplier<Iban, AccountCreated, BalanceUpdated> initializeAccountBalance) {
    KStream<Iban, BalanceUpdated> accountCreatedStream = streamsBuilder
      .stream(
        topics.accountCreated(),
        Consumed.with(ledgerSerdes.iban(), ledgerSerdes.accountCreated())
                .withName("consume-" + topics.accountCreated()))
      .processValues(
        initializeAccountBalance,
        Named.as("initialize-account-balance"),
        stores.accountCreated(),
        stores.balanceUpdated());
    accountCreatedStream.to(topics.balanceUpdated());
    return accountCreatedStream;
  }
  
  @Bean
  public KStream<Iban, BalanceUpdated> updateBalanceStream(
    FixedKeyProcessorSupplier<Iban, MovementApplied, BalanceUpdated> updateAccountBalance) {
    KStream<Iban, BalanceUpdated> balanceUpdatedStream = streamsBuilder
      .stream(topics.movementApplied(),
        Consumed.with(ledgerSerdes.iban(), ledgerSerdes.movementApplied())
                .withName("consume-" + topics.movementApplied()))
      .processValues(
        updateAccountBalance,
        Named.as("update-account-balance"),
        stores.movementsList(),
        stores.balanceUpdated());
    balanceUpdatedStream.to(topics.balanceUpdated());
    return balanceUpdatedStream;
  }
  
}
