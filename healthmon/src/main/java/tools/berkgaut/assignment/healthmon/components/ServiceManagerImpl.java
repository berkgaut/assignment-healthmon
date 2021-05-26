package tools.berkgaut.assignment.healthmon.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.berkgaut.assignment.healthmon.apiobjects.Service;
import tools.berkgaut.assignment.healthmon.entities.CheckStatus;
import tools.berkgaut.assignment.healthmon.entities.HealthCheckEntity;
import tools.berkgaut.assignment.healthmon.entities.ServiceEntity;
import tools.berkgaut.assignment.healthmon.entities.ServiceStatus;
import tools.berkgaut.assignment.healthmon.exceptions.HealthmonException;
import tools.berkgaut.assignment.healthmon.repositories.HealthCheckRepository;
import tools.berkgaut.assignment.healthmon.repositories.ServiceRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

@Component
public class ServiceManagerImpl implements ServiceManager {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    HealthCheckRepository healthCheckRepository;

    @Override
    @Transactional
    public List<Service> getServices() throws HealthmonException {
        return serviceRepository.findAll().stream()
                .map(this::serviceEntityToPojo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Service createService(Service createData) throws HealthmonException {
        // TODO: handle missing data and validation
        ServiceEntity newService = ServiceEntity.builder()
                .healthCheckUrl(createData.getUrl())
                .name(createData.getName())
                .healthCheckTimeoutMillis(createData.getTimeoutMillis())
                .build();
        serviceRepository.save(newService);
        HealthCheckEntity newHealthCheck = HealthCheckEntity.builder()
                .service(newService)
                .serviceStatus(ServiceStatus.UNKNOWN)
                .checkStatus(CheckStatus.PENDING)
                .build();
        healthCheckRepository.save(newHealthCheck);
        newService.setHealthCheck(newHealthCheck);
        return serviceEntityToPojo(newService);
    }

    @Override
    @Transactional
    public Service updateService(Long serviceId, Service updateData) throws HealthmonException {
        // TODO: handle validation
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new HealthmonException(404, "service not found"));
        if (updateData.getName() != null) {
            service.setName(updateData.getName());
        }
        if (updateData.getUrl() != null) {
            service.setHealthCheckUrl(updateData.getUrl());
        }
        if (updateData.getTimeoutMillis() != null) {
            service.setHealthCheckTimeoutMillis(updateData.getTimeoutMillis());
        }
        serviceRepository.save(service);
        return serviceEntityToPojo(service);
    }

    @Override
    @Transactional
    public void deleteService(Long serviceId) throws HealthmonException {
        if (serviceRepository.existsById(serviceId)) {
            serviceRepository.deleteById(serviceId);
        } else {
            throw new HealthmonException(404, "service not found");
        }
    }

    private Service serviceEntityToPojo(ServiceEntity service) {
        return Service.builder()
                .id(service.getId().toString())
                .url(service.getHealthCheckUrl())
                .name(service.getName())
                .timeoutMillis(service.getHealthCheckTimeoutMillis())
                .created(timestampToString(service.getCreated()))
                .updated(timestampToString(service.getUpdated()))
                .status(service.getHealthCheck().getServiceStatus().toString())
                .build();
    }

    private String timestampToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            return ISO_INSTANT.format(localDateTime.atZone(ZoneOffset.UTC));
        }
    }
}
