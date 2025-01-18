package tech.edwyn.ledger.documentation.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@ActiveProfiles("documentation")
@AutoConfigureJson
public class CucumberConfiguration {
}
