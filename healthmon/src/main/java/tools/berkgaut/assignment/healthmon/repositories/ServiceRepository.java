package tools.berkgaut.assignment.healthmon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.berkgaut.assignment.healthmon.entities.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}
