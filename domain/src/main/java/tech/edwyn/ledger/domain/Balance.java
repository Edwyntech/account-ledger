package tech.edwyn.ledger.domain;

import org.iban4j.Iban;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import java.time.Instant;

public record Balance(Iban iban, Money amount, Instant timestamp) {
  
  public static Balance zero(Account account) {
    CurrencyUnit currencyUnit = Account.currencyFrom(account.iban());
    return new Balance(account.iban(), Money.zero(currencyUnit), account.createdAt());
  }
}
