package tech.edwyn.ledger.documentation.config;

import io.cucumber.spring.ScenarioScope;
import org.apache.kafka.streams.TopologyTestDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import tech.edwyn.ledger.application.LedgerProperties;
import tech.edwyn.ledger.application.infrastructure.stores.KeyValueAccountStore;
import tech.edwyn.ledger.application.infrastructure.stores.KeyValueBalanceStore;
import tech.edwyn.ledger.application.infrastructure.stores.KeyValueMovementStore;
import tech.edwyn.ledger.application.streams.serde.LedgerSerdes;

import java.util.Objects;

import static org.springframework.context.annotation.ScopedProxyMode.NO;

@Configuration
public class FeaturesConfiguration {
  private static final Logger log = LoggerFactory.getLogger(FeaturesConfiguration.class);
  
  private final LedgerProperties.Topics topics;
  private final LedgerProperties.Stores stores;
  
  public FeaturesConfiguration(LedgerProperties ledgerProperties) {
    log.trace("init - ledgerProperties: {}", ledgerProperties);
    
    this.topics = ledgerProperties.topics();
    this.stores = ledgerProperties.stores();
  }
  
  @Bean
  @ScenarioScope(proxyMode = NO)
  public TopologyTestDriver topologyTestDriver(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
    log.trace("topologyTestDriver");
    
    return new TopologyTestDriver(
      Objects.requireNonNull(streamsBuilderFactoryBean.getTopology()),
      streamsBuilderFactoryBean.getStreamsConfiguration());
  }
  
  @Bean
  @ScenarioScope
  public KeyValueAccountStore accountStore(TopologyTestDriver topologyTestDriver) {
    log.trace("accountStore");
    
    return new KeyValueAccountStore(() ->
      topologyTestDriver.getKeyValueStore(stores.accountCreated()));
  }
  
  @Bean
  @ScenarioScope
  public KeyValueBalanceStore balanceStore(TopologyTestDriver topologyTestDriver) {
    log.trace("balanceStore");
    
    return new KeyValueBalanceStore(() ->
      topologyTestDriver.getKeyValueStore(stores.balanceUpdated()));
  }
  
  @Bean
  @ScenarioScope
  public KeyValueMovementStore movementStore(TopologyTestDriver topologyTestDriver) {
    log.trace("movementStore");
    
    return new KeyValueMovementStore(() ->
      topologyTestDriver.getKeyValueStore(stores.movementsList()));
  }
  
  @Bean
  @ScenarioScope(proxyMode = NO)
  @SuppressWarnings("resource")
  public World world(TopologyTestDriver topologyTestDriver, LedgerSerdes ledgerSerdes) {
    log.trace("world");
    
    var accountCreatedTopic = topologyTestDriver.createInputTopic(
      topics.accountCreated(),
      ledgerSerdes.iban()
                  .serializer(),
      ledgerSerdes.accountCreated()
                  .serializer());
    var movementAppliedTopic = topologyTestDriver.createInputTopic(
      topics.movementApplied(),
      ledgerSerdes.iban()
                  .serializer(),
      ledgerSerdes.movementApplied()
                  .serializer());
    return new World(accountCreatedTopic, movementAppliedTopic);
  }
  
}
