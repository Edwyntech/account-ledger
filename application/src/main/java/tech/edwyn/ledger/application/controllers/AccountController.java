package tech.edwyn.ledger.application.controllers;

import org.iban4j.Iban;
import org.springframework.web.bind.annotation.*;
import tech.edwyn.ledger.domain.Account;
import tech.edwyn.ledger.domain.Balance;
import tech.edwyn.ledger.domain.LedgerService;
import tech.edwyn.ledger.domain.Movement;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("ledger/accounts")
public class AccountController {
  
  private final LedgerService ledgerService;
  
  public AccountController(LedgerService ledgerService) {
    this.ledgerService = ledgerService;
  }
  
  @GetMapping
  public AccountsListDto getAccountsList(@RequestParam UUID customerId) {
    List<Account> accountsList = ledgerService.getAccountsList(customerId);
    return AccountsListDto.from(customerId, accountsList);
  }
  
  @GetMapping("{iban}/balance")
  public AccountBalanceDto getAccountBalance(@PathVariable Iban iban) {
    Balance balance = ledgerService.getAccountBalance(iban);
    return AccountBalanceDto.from(balance);
  }
  
  @GetMapping("{iban}/movements")
  public AccountMovementsListDto getMovementsList(@PathVariable Iban iban) {
    List<Movement> movementsList = ledgerService.getMovementsList(iban);
    return AccountMovementsListDto.from(iban, movementsList);
  }
  
}
