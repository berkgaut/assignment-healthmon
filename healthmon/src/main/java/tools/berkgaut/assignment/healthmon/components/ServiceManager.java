package tools.berkgaut.assignment.healthmon.components;

import tools.berkgaut.assignment.healthmon.apiobjects.Service;
import tools.berkgaut.assignment.healthmon.exceptions.HealthmonException;

import java.util.List;

public interface ServiceManager {

    List<Service> getServices() throws HealthmonException;

    Service createService(Service createData) throws HealthmonException;

    Service updateService(Long serviceId, Service updateData) throws HealthmonException;

    void deleteService(Long serviceId) throws HealthmonException;
}
