package tech.edwyn.ledger.documentation.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tech.edwyn.ledger.application.LedgerApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = LedgerApplication.class)
@AutoConfigureJson
@AutoConfigureMockMvc
@ActiveProfiles({"test", "documentation"})
public class CucumberConfiguration {
}
