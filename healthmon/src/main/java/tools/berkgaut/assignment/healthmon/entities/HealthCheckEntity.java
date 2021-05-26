package tools.berkgaut.assignment.healthmon.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import tools.berkgaut.assignment.healthmon.converter.CheckStatusConverter;
import tools.berkgaut.assignment.healthmon.converter.ServiceStatusConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="healthcheck")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthCheckEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UpdateTimestamp
    private LocalDateTime updated;

    @OneToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @Column(name="service_status")
    @Convert(converter = ServiceStatusConverter.class)
    private ServiceStatus serviceStatus;

    @Column(name="check_status")
    @Convert(converter = CheckStatusConverter.class)
    private CheckStatus checkStatus;
}
