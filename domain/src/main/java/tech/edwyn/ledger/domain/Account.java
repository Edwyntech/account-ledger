package tech.edwyn.ledger.domain;

import org.iban4j.Iban;

import javax.money.CurrencyQuery;
import javax.money.CurrencyQueryBuilder;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record Account(Iban iban, UUID customerId, String label, Instant createdAt) {
  
  public Account(Iban iban, UUID customerId, String label, Instant createdAt) {
    this.iban = iban;
    this.customerId = customerId;
    this.label = Optional.ofNullable(label)
                         .orElse(typeFrom(iban).defaultLabel());
    this.createdAt = createdAt;
  }
  
  public static AccountType typeFrom(Iban iban) {
    var accountCode = iban.getBranchCode()
                          .substring(3, 5);
    return AccountType.ofCode(accountCode);
  }
  
  public static CurrencyUnit currencyFrom(Iban iban) {
    var currencyNumericCode = iban.getBranchCode()
                                  .substring(0, 3);
    CurrencyQuery currencyQuery = CurrencyQueryBuilder.of()
                                                      .setNumericCodes(Integer.parseInt(currencyNumericCode))
                                                      .build();
    return Monetary.getCurrency(currencyQuery);
  }
}
