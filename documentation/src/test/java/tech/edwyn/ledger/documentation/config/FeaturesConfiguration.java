package tech.edwyn.ledger.documentation.config;

import io.cucumber.spring.ScenarioScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.context.annotation.ScopedProxyMode.NO;

@SpringBootConfiguration
public class FeaturesConfiguration {
  private static final Logger log = LoggerFactory.getLogger(FeaturesConfiguration.class);
  
  @Bean
  @ScenarioScope(proxyMode = NO)
  public World world() {
    log.trace("world");
    
    return new World();
  }
  
}
