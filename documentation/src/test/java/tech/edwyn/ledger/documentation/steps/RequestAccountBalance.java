package tech.edwyn.ledger.documentation.steps;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Lorsque;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import tech.edwyn.ledger.documentation.config.World;
import tech.edwyn.ledger.domain.Customer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RequestAccountBalance {
  private static final Logger log = LoggerFactory.getLogger(RequestAccountBalance.class);
  
  private final World   world;
  private final MockMvc mvc;
  
  public RequestAccountBalance(World world, MockMvc mvc) {
    this.world = world;
    this.mvc = mvc;
  }
  
  @Lorsque("{customer} requiert le solde du compte {iban}")
  public void customerRequestBalanceForIban(Customer customer, Iban iban) throws Exception {
    log.trace("customerRequestBalanceForIban - customer: {}, iban: {}", customer, iban);
    
    world.resultActions = mvc.perform(get("/ledger/accounts/{iban}/balance", iban))
                             .andDo(print());
  }
  
  @Alors("le solde retourné doit être")
  public void returnedAccountBalanceMustBe(String expectedAccountBalance) throws Exception {
    log.trace("returnedAccountBalanceMustBe - expectedAccountBalance: {}", expectedAccountBalance);
    
    world.resultActions.andExpect(status().isOk())
                       .andExpect(content().json(expectedAccountBalance));
  }
}
