package tools.berkgaut.assignment.healthmon.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.berkgaut.assignment.healthmon.entities.CheckStatus;
import tools.berkgaut.assignment.healthmon.entities.ServiceStatus;
import tools.berkgaut.assignment.healthmon.repositories.HealthCheckRepository;

import javax.transaction.Transactional;
import java.time.Duration;

@Component
@Slf4j
public class HealthMonitorImpl implements HealthMonitor {
    @Autowired
    HealthCheckRepository healthCheckRepository;

    @Autowired
    HealthChecker healthChecker;

    @Override
    @Transactional
    public void performChecks() {
        try {
            healthCheckRepository.reScheduleCompletedChecks();
            healthCheckRepository.getPendingChecks().forEach(healthCheck -> {
                Duration timeout = Duration.ofMillis(healthCheck.getService().getHealthCheckTimeoutMillis());
                String healthCheckUrl = healthCheck.getService().getHealthCheckUrl();
                boolean success = healthChecker.performCheck(timeout, healthCheckUrl);
                healthCheck.setServiceStatus(success ? ServiceStatus.OK : ServiceStatus.FAIL);
                healthCheck.setCheckStatus(CheckStatus.COMPLETE);
                healthCheckRepository.save(healthCheck);
            });
        } catch (Exception e) {
            log.error("performChecks", e);
        }
    }
}
