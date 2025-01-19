package tech.edwyn.ledger.domain;

import org.iban4j.Iban;
import org.javamoney.moneta.Money;

import java.time.Instant;

public record Movement(Iban iban, Money amount, Instant timestamp) {
}
