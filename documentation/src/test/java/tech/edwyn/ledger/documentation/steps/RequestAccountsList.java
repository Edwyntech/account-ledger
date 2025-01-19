package tech.edwyn.ledger.documentation.steps;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Lorsque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import tech.edwyn.ledger.documentation.config.World;
import tech.edwyn.ledger.domain.Customer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RequestAccountsList {
  private static final Logger log = LoggerFactory.getLogger(RequestAccountsList.class);
  
  private final World   world;
  private final MockMvc mvc;
  
  public RequestAccountsList(World world, MockMvc mvc) {
    this.world = world;
    this.mvc = mvc;
  }
  
  @Lorsque("{customer} requiert la liste de ses comptes")
  public void customerRequestsAccountsList(Customer customer) throws Exception {
    log.trace("customerRequestsAccountsList - customer: {}", customer);
    
    world.resultActions = mvc.perform(get("/ledger/accounts")
                               .param("customerId", customer.id()
                                                            .toString()))
                             .andDo(print());
  }
  
  @Alors("la liste de comptes retournée doit être")
  public void returnedAccountsListMustBe(String expectedAccountsList) throws Exception {
    log.trace("returnedAccountsListMustBe - expectedAccountsList: {}", expectedAccountsList);
    
    world.resultActions.andExpect(status().isOk())
                       .andExpect(content().json(expectedAccountsList));
  }
  
}
