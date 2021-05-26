package tools.berkgaut.assignment.healthmon.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "enable_scheduled_actions", matchIfMissing = false, havingValue = "true")
public class HealthMonScheduler {

    @Autowired
    HealthMonitor healthMonitor;

    @Scheduled(cron = "0 */5 * * * *")
    public void performChecks() {
        healthMonitor.performChecks();
    }

}
