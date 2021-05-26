package tools.berkgaut.assignment.healthmon.components;

import tools.berkgaut.assignment.healthmon.entities.HealthCheckEntity;

import java.time.Duration;

public interface HealthChecker {
    boolean performCheck(Duration timeout, String healthCheckUrl);
}
