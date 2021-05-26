package tools.berkgaut.assignment.healthmon.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="service")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    private String name;

    @Column(name="healthcheck_url")
    private String healthCheckUrl;

    @Column(name="healthcheck_timeout_ms")
    private int healthCheckTimeoutMillis;

    @OneToOne(mappedBy = "service", cascade = CascadeType.REMOVE)
    private HealthCheckEntity healthCheck;
}
