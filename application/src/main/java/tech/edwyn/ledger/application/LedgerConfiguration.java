package tech.edwyn.ledger.application;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.edwyn.ledger.domain.AccountRepository;
import tech.edwyn.ledger.domain.BalanceRepository;
import tech.edwyn.ledger.domain.LedgerService;
import tech.edwyn.ledger.domain.MovementRepository;
import tech.edwyn.ledger.ports.AccountStore;
import tech.edwyn.ledger.ports.BalanceStore;
import tech.edwyn.ledger.ports.MovementStore;

@Configuration
@EnableConfigurationProperties(LedgerProperties.class)
public class LedgerConfiguration {
  
  @Bean
  public AccountRepository accountRepository(AccountStore accountStore) {
    return new AccountRepository(accountStore);
  }
  
  @Bean
  public BalanceRepository balanceRepository(BalanceStore balanceStore) {
    return new BalanceRepository(balanceStore);
  }
  
  @Bean
  public MovementRepository movementRepository(MovementStore movementStore) {
    return new MovementRepository(movementStore);
  }
  
  @Bean
  public LedgerService ledgerService(AccountRepository accountRepository,
                                     BalanceRepository balanceRepository,
                                     MovementRepository movementRepository) {
    return new LedgerService(accountRepository, balanceRepository, movementRepository);
  }
}
