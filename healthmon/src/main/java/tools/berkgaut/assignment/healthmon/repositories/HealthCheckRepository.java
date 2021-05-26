package tools.berkgaut.assignment.healthmon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tools.berkgaut.assignment.healthmon.entities.HealthCheckEntity;

import java.util.List;

public interface HealthCheckRepository extends JpaRepository<HealthCheckEntity, Long> {
    @Query("select healthcheck from HealthCheckEntity healthcheck where healthcheck.checkStatus = tools.berkgaut.assignment.healthmon.entities.CheckStatus.PENDING")
    List<HealthCheckEntity> getPendingChecks();

    @Modifying
    @Query("update HealthCheckEntity healthcheck set healthcheck.checkStatus = tools.berkgaut.assignment.healthmon.entities.CheckStatus.PENDING" +
            " where healthcheck.checkStatus = tools.berkgaut.assignment.healthmon.entities.CheckStatus.COMPLETE")
    void scheduleCompletedChecks();
}
