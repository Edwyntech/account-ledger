package tech.edwyn.ledger.application.config;

import org.apache.avro.specific.SpecificRecord;
import org.iban4j.Iban;
import org.javamoney.moneta.Money;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import tech.edwyn.ledger.application.LedgerProperties;
import tech.edwyn.ledger.avro.account.AccountCreated;
import tech.edwyn.ledger.avro.account.MovementApplied;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.iban4j.IbanFormat.Default;

@Configuration
@Profile("docker")
public class DockerConfiguration {
  private final LedgerProperties.Topics             topics;
  private final KafkaTemplate<Iban, SpecificRecord> kafkaTemplate;
  
  public DockerConfiguration(LedgerProperties ledgerProperties, KafkaTemplate<Iban, SpecificRecord> kafkaTemplate) {
    this.topics = ledgerProperties.topics();
    this.kafkaTemplate = kafkaTemplate;
  }
  
  @Bean
  public UUID customerId() {
    return UUID.fromString("3c128dc9-8166-4b30-b821-59a02708f3cd");
  }
  
  @Bean
  public Iban iban() {
    return Iban.valueOf("FR97 1234 5978 0100 0000 0000 134", Default);
  }
  
  @Bean
  public CommandLineRunner sendAccountCreated(UUID customerId, Iban iban) {
    return args -> kafkaTemplate.send(
      topics.accountCreated(),
      iban,
      AccountCreated.newBuilder()
                    .setTimestamp(Instant.now())
                    .setCustomerId(customerId)
                    .setIban(iban)
                    .setLabel("Perso")
                    .build());
  }
  
  @Bean
  public CommandLineRunner sendMovementApplied(Iban iban) {
    return args -> kafkaTemplate.send(
      topics.movementApplied(),
      iban,
      MovementApplied.newBuilder()
                     .setTimestamp(Instant.now())
                     .setIban(iban)
                     .setAmount(Money.of(new BigDecimal(1000), "EUR"))
                     .build());
  }
  
}
