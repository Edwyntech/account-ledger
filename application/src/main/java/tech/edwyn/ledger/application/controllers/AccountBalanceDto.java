package tech.edwyn.ledger.application.controllers;

import tech.edwyn.ledger.domain.Balance;

import javax.money.format.MonetaryFormats;
import java.time.Instant;

import static java.util.Locale.FRANCE;

public record AccountBalanceDto(
  String iban,
  String amount,
  Instant timestamp) {
  
  public static AccountBalanceDto from(Balance balance) {
    return new AccountBalanceDto(
      balance.iban()
             .toFormattedString(),
      MonetaryFormats.getAmountFormat(FRANCE)
                     .format(balance.amount()),
      balance.timestamp());
  }
}
