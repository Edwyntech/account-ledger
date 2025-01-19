package tech.edwyn.ledger.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication(scanBasePackages = "tech.edwyn.ledger")
public class LedgerApplication {
  public static void main(String[] args) {
    run(LedgerApplication.class, args);
  }
  
}
