package stepDefinitions;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import au.com.telstra.simcardactivator.SimCardActivator;

@CucumberContextConfiguration
@SpringBootTest(classes = SimCardActivator.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SimCardActivatorApplicationTests {
    // This class is intentionally empty.
    // It only serves as a Spring context configuration holder for Cucumber tests.
}
