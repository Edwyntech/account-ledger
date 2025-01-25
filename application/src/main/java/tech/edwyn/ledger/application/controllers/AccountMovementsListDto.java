package tech.edwyn.ledger.application.controllers;

import org.iban4j.Iban;
import tech.edwyn.ledger.domain.Movement;

import javax.money.format.MonetaryFormats;
import java.time.Instant;
import java.util.List;

import static java.util.Locale.FRANCE;

public record AccountMovementsListDto(String iban, List<AccountMovementDto> movements) {
  public static AccountMovementsListDto from(Iban iban, List<Movement> movementsList) {
    return new AccountMovementsListDto(
      iban.toFormattedString(),
      movementsList.stream()
                   .map(AccountMovementDto::from)
                   .toList());
  }
  
  public record AccountMovementDto(String amount, Instant timestamp) {
    public static AccountMovementDto from(Movement movement) {
      return new AccountMovementDto(
        MonetaryFormats.getAmountFormat(FRANCE)
                       .format(movement.amount()),
        movement.timestamp());
    }
  }
}
