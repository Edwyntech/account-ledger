package tech.edwyn.ledger.documentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

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
}
