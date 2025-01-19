package tech.edwyn.ledger.application.streams;

public class OverdraftForbidden extends RuntimeException {
  
  public OverdraftForbidden() {
    super("Overdraft forbidden");
  }
  
}
