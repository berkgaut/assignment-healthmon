package tools.berkgaut.assignment.healthmon.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tools.berkgaut.assignment.healthmon.entities.CheckStatus;
import tools.berkgaut.assignment.healthmon.entities.HealthCheckEntity;
import tools.berkgaut.assignment.healthmon.entities.ServiceStatus;

import java.time.Duration;

@Component
@Slf4j
public class HealthCheckerImpl implements HealthChecker {

    @Override
    public boolean performCheck(Duration timeout, String healthCheckUrl) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setReadTimeout(timeout)
                .setConnectTimeout(timeout)
                .build();
        RequestEntity<Void> request = RequestEntity.get(healthCheckUrl).build();
        boolean success;
        try {
            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
            success = response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            success = false;
        }
        log.info("Check result for {}: succeed={}", healthCheckUrl, success);
        return success;
    }
}
