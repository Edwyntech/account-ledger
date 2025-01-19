package tech.edwyn.ledger.application.config;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import tech.edwyn.ledger.application.LedgerProperties;
import tech.edwyn.ledger.application.infrastructure.stores.KeyValueAccountStore;
import tech.edwyn.ledger.application.infrastructure.stores.KeyValueBalanceStore;
import tech.edwyn.ledger.application.infrastructure.stores.KeyValueMovementStore;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class StreamsConfiguration {
  private final LedgerProperties.Stores stores;
  
  public StreamsConfiguration(LedgerProperties ledgerProperties) {
    this.stores = ledgerProperties.stores();
  }
  
  @Bean
  public KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
    return new KafkaStreamsInteractiveQueryService(streamsBuilderFactoryBean);
  }
  
  @Bean
  @Profile("!test")
  public KeyValueAccountStore accountStore(KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService) {
    return new KeyValueAccountStore(() -> kafkaStreamsInteractiveQueryService.retrieveQueryableStore(
      stores.accountCreated(),
      QueryableStoreTypes.keyValueStore()));
  }
  
  @Bean
  @Profile("!test")
  public KeyValueBalanceStore balanceStore(KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService) {
    return new KeyValueBalanceStore(() -> kafkaStreamsInteractiveQueryService.retrieveQueryableStore(
      stores.balanceUpdated(),
      QueryableStoreTypes.keyValueStore()));
  }
  
  @Bean
  @Profile("!test")
  public KeyValueMovementStore movementStore(KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService) {
    return new KeyValueMovementStore(() -> kafkaStreamsInteractiveQueryService.retrieveQueryableStore(
      stores.movementsList(),
      QueryableStoreTypes.keyValueStore()));
  }
}
