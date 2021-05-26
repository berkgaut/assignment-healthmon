package tools.berkgaut.assignment.healthmon.entities;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public enum ServiceStatus {
    UNKNOWN(0),
    OK(1),
    FAILURE(2)
    ;

    private final int value;

    private static final Map<Integer, ServiceStatus> integersToValues = stream(values())
            .collect(Collectors.toMap(ServiceStatus::getValue, Function.identity()));

    ServiceStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ServiceStatus fromInteger(Integer value) {
        return integersToValues.get(value);
    }
}
