package tech.edwyn.ledger.domain;

import java.util.EnumSet;

public enum AccountType {
  CURRENT("01", "Compte courant"),
  SAVINGS("02", "Compte d'épargne");
  
  private final String defaultLabel;
  private final String code;
  
  AccountType(String code, String defaultLabel) {
    this.code = code;
    this.defaultLabel = defaultLabel;
  }
  
  public static AccountType ofCode(String accountCode) {
    return EnumSet.allOf(AccountType.class)
                  .stream()
                  .filter(t -> t.code.equals(accountCode))
                  .findFirst()
                  .orElseThrow(() -> new IllegalArgumentException("unexpected account code " + accountCode));
  }
  
  public String defaultLabel() {
    return defaultLabel;
  }
}
