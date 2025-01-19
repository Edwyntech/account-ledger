package tech.edwyn.ledger.documentation.steps;

import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.java.fr.Etqu;
import io.cucumber.java.fr.Etque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.ledger.documentation.config.World;
import tech.edwyn.ledger.domain.Account;
import tech.edwyn.ledger.domain.Customer;
import tech.edwyn.ledger.domain.Movement;

import java.util.List;

public class SetupWorld {
  private static final Logger log = LoggerFactory.getLogger(SetupWorld.class);
  
  private final World world;
  
  public SetupWorld(World world) {
    this.world = world;
  }
  
  @Etantdonné("nos clients")
  public void ourCustomers(List<Customer> customers) {
    log.trace("ourCustomers - customers: {}", customers);
    
    world.customers = customers;
  }
  
  @Etantdonnéque("CEL n'a créé aucun compte")
  public void celHasCreatedNoAccount() {
    log.trace("celHasCreatedNoAccount");
  }
  
  @Etantdonnéque("CEL a créé le(s) compte(s)")
  public void celHasCreatedAccounts(List<Account> accounts) {
    log.trace("celHasCreatedAccounts - accounts: {}", accounts);
    
    try {
      world.create(accounts);
    } catch (Exception e) {
      log.error("Error while creating accounts: {}", e.getMessage());
    }
  }
  
  @Etqu("aucun mouvement n'est constaté")
  public void noMovementIsObserved() {
    log.trace("noMovementIsObserved");
  }
  
  @Etque("les mouvements suivants ont été constatés")
  public void followingMovementsHaveBeenObserved(List<Movement> movements) {
    log.trace("followingMovementsHaveBeenObserved - movements: {}", movements);
    
    try {
      world.observe(movements);
    } catch (Exception e) {
      log.error("Error while publishing movements: {}", e.getMessage());
    }
  }
}
