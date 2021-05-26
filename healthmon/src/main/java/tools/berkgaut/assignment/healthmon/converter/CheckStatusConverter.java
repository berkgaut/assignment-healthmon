package tools.berkgaut.assignment.healthmon.converter;

import tools.berkgaut.assignment.healthmon.entities.CheckStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class CheckStatusConverter implements AttributeConverter<CheckStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CheckStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public CheckStatus convertToEntityAttribute(Integer dbData) {
        return CheckStatus.fromInteger(dbData == null ? 0 : dbData);
    }
}
