package au.com.telstra.simcardactivator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class SimController {

    @Autowired
    private SimCardRepository repository;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/activate")
    public Map<String, Object> activateSim(@RequestBody Map<String, String> request) {
        String iccid = request.get("iccid");
        String customerEmail = request.get("customerEmail");

        // Prepare actuator request
        Map<String, String> actuatorRequest = new HashMap<>();
        actuatorRequest.put("iccid", iccid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(actuatorRequest, headers);

        // Call the actuator
        ResponseEntity<Map> response = restTemplate.exchange(
                "http://localhost:8444/actuate",
                HttpMethod.POST,
                entity,
                Map.class
        );

        boolean success = (Boolean) response.getBody().get("success");

        // Save to DB
        SimCardRecord record = new SimCardRecord(iccid, customerEmail, success);
        repository.save(record);

        // Response to client
        Map<String, Object> result = new HashMap<>();
        result.put("iccid", iccid);
        result.put("customerEmail", customerEmail);
        result.put("activationStatus", success ? "success" : "failure");

        return result;
    }

    @GetMapping("/sim")
    public ResponseEntity<?> getSimById(@RequestParam Long simCardId) {
        Optional<SimCardRecord> record = repository.findById(simCardId);

        if (record.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("iccid", record.get().getIccid());
            response.put("customerEmail", record.get().getCustomerEmail());
            response.put("active", record.get().isActive());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SIM record not found");
        }
    }
}