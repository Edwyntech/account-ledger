package tech.edwyn.ledger.documentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.*;
import org.iban4j.Iban;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.ledger.domain.Account;
import tech.edwyn.ledger.domain.Customer;
import tech.edwyn.ledger.domain.Movement;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import static org.iban4j.IbanFormat.Default;

public class ParameterTypes {
  private static final Logger log = LoggerFactory.getLogger(ParameterTypes.class);
  
  private final ObjectMapper objectMapper;
  private final World        world;
  
  public ParameterTypes(World world,
                        ObjectMapper objectMapper) {
    log.trace("init - world: {}, objectMapper: {}", world, objectMapper);
    
    this.world = world;
    this.objectMapper = objectMapper;
  }
  
  @DefaultParameterTransformer
  @DefaultDataTableEntryTransformer
  @DefaultDataTableCellTransformer
  public Object transformer(Object fromValue, Type toValueType) {
    log.trace("transformer - fromValue: {}, toValueType: {}", fromValue, toValueType);
    
    return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
  }
  
  @ParameterType(".+?")
  public Customer customer(String firstName) {
    return world.customerWithFirstName(firstName);
  }
  
  @ParameterType("\\d{2}/\\d{2}/\\d{4} à \\d{2}:\\d{2}:\\d{2}")
  public Instant instant(String instantString) {
    LocalDateTime instantTime = LocalDateTime.parse(instantString, DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss"));
    return instantTime.toInstant(ZoneOffset.UTC);
  }
  
  @ParameterType("FR\\d{2} 1234 5\\d{3} 0[1|2]00 \\d{4} \\d{4} \\d{3}")
  public Iban iban(String ibanString) {
    return Iban.valueOf(ibanString, Default);
  }
  
  @DataTableType
  public Customer customer(Map<String, String> entries) {
    log.trace("customer - entries: {}", entries);
    
    var id        = entries.get("Identifiant");
    var lastName  = entries.get("Nom de famille");
    var firstName = entries.get("Prénom");
    var email     = entries.get("E-mail");
    
    return new Customer(UUID.fromString(id), lastName, firstName, email);
  }
  
  @DataTableType
  public Account account(Map<String, String> entries) {
    log.trace("account - entries: {}", entries);
    
    var ibanString        = entries.get("IBAN");
    var customerFirstName = entries.get("Client");
    var label             = entries.get("Libellé");
    var createdAt         = entries.get("Créé le");
    
    var customer = world.customerWithFirstName(customerFirstName);
    
    return new Account(iban(ibanString), customer.id(), label, instant(createdAt));
  }
  
  @DataTableType
  public Movement movement(Map<String, String> entries) {
    log.trace("movement - entries: {}", entries);
    
    var timestampString = entries.get("Instant");
    var ibanString      = entries.get("IBAN");
    var amountString    = entries.get("Montant");
    
    return new Movement(iban(ibanString), Money.parse(amountString), instant(timestampString));
  }
  
}
