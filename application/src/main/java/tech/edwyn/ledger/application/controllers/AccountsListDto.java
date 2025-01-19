package tech.edwyn.ledger.application.controllers;

import tech.edwyn.ledger.domain.Account;

import java.util.List;
import java.util.UUID;

public record AccountsListDto(UUID customerId, List<AccountDto> accounts) {
  public static AccountsListDto from(UUID customerId, List<Account> accountsList) {
    return new AccountsListDto(
      customerId,
      accountsList.stream()
                  .map(AccountDto::from)
                  .toList());
  }
  
  public record AccountDto(String iban, String label) {
    public static AccountDto from(Account account) {
      return new AccountDto(
        account.iban()
               .toFormattedString(),
        account.label());
    }
  }
}
