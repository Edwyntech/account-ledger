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

public class RequestMovementsList {
  private static final Logger log = LoggerFactory.getLogger(RequestMovementsList.class);
  
  private final World   world;
  private final MockMvc mvc;
  
  public RequestMovementsList(World world, MockMvc mvc) {
    this.world = world;
    this.mvc = mvc;
  }
  
  @Lorsque("{customer} requiert la liste de mouvements sur le compte {iban}")
  public void customerRequestsMovementsListForIban(Customer customer, Iban iban) throws Exception {
    log.trace("customerRequestsMovementsListOnAccount - customer: {}, iban: {}", customer, iban);
    
    world.resultActions = mvc.perform(get("/ledger/accounts/{iban}/movements", iban))
                             .andDo(print());
  }
  
  @Alors("la liste de mouvements retournée doit être")
  public void returnedMovementsListMustBe(String expectedMovementsList) throws Exception {
    log.trace("returnedMovementsListMustBe - expectedMovementsList: {}", expectedMovementsList);
    
    world.resultActions.andExpect(status().isOk())
                       .andExpect(content().json(expectedMovementsList));
  }
}
