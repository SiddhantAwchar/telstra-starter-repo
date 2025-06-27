package stepDefinitions;

import io.cucumber.java.en.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SimCardActivatorStepDefinitions {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private String iccid;
    private String customerEmail;
    private long currentId;
    private Map<String, Object> activationResponse;

    @Given("the SIM card ICCID is {string} and the customer email is {string}")
    public void setSimCardDetails(String iccid, String email) {
        this.iccid = iccid;
        this.customerEmail = email;
    }

    @When("I activate the SIM card")
    public void activateSimCard() {
        Map<String, String> body = new HashMap<>();
        body.put("simId", iccid);
        body.put("customer", customerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity("http://localhost:8080/activate", request, Map.class);

        currentId++; // simulate auto-increment id assumption
    }

    @Then("the activation should be successful with ID {int}")
    public void checkSuccess(int id) {
        ResponseEntity<Map> response = restTemplate.getForEntity("http://localhost:8080/query?simCardId=" + id, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        activationResponse = response.getBody();
        assertNotNull(activationResponse);
        assertEquals(true, activationResponse.get("active"));
    }

    @Then("the activation should be unsuccessful with ID {int}")
    public void checkFailure(int id) {
        ResponseEntity<Map> response = restTemplate.getForEntity("http://localhost:8080/query?simCardId=" + id, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        activationResponse = response.getBody();
        assertNotNull(activationResponse);
        assertEquals(false, activationResponse.get("active"));
    }
}
