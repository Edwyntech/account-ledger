package tech.edwyn.ledger.domain;

import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class LedgerService {
  private static final Logger log = LoggerFactory.getLogger(LedgerService.class);
  
  private final AccountRepository  accountRepository;
  private final BalanceRepository  balanceRepository;
  private final MovementRepository movementRepository;
  
  public LedgerService(AccountRepository accountRepository,
                       BalanceRepository balanceRepository,
                       MovementRepository movementRepository) {
    this.accountRepository = accountRepository;
    this.balanceRepository = balanceRepository;
    this.movementRepository = movementRepository;
  }
  
  public List<Account> getAccountsList(UUID customerId) {
    log.trace("getAccountsList - customerId: {}", customerId);
    
    return accountRepository.findAllByCustomerId(customerId);
  }
  
  public Balance getAccountBalance(Iban iban) {
    log.trace("getAccountBalance - iban: {}", iban);
    
    Account account = accountRepository.findByIban(iban)
                                       .orElseThrow(accountNotFound(iban));
    
    return balanceRepository.findByIban(account.iban())
                            .orElseGet(() -> Balance.zero(account));
  }
  
  public List<Movement> getMovementsList(Iban iban) {
    log.trace("getMovementsList - iban: {}", iban);
    
    Account account = accountRepository.findByIban(iban)
                                       .orElseThrow(accountNotFound(iban));
    
    return movementRepository.findAllByIban(account.iban());
  }
  
  private Supplier<? extends RuntimeException> accountNotFound(Iban iban) {
    return () -> new RuntimeException("Account not found: " + iban);
  }
  
}
