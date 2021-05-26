package tools.berkgaut.assignment.healthmon.converter;

import tools.berkgaut.assignment.healthmon.entities.ServiceStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class ServiceStatusConverter implements AttributeConverter<ServiceStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ServiceStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public ServiceStatus convertToEntityAttribute(Integer dbData) {
        return ServiceStatus.fromInteger(dbData == null ? 0 : dbData);
    }
}
