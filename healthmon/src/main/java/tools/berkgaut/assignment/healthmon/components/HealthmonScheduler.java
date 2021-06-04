package tools.berkgaut.assignment.healthmon.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HealthmonScheduler {

    @Autowired
    HealthMonitor healthMonitor;

    @Scheduled(cron = "${healthmon.schedule:-}")
    public void performChecks() {
        log.info("Perform checks");
        healthMonitor.performChecks();
    }
}
