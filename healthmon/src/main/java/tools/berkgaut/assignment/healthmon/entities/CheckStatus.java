package tools.berkgaut.assignment.healthmon.entities;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public enum CheckStatus {
    PENDING(0),
    IN_PROGRESS(1),
    COMPLETE(2)
    ;

    private final int value;

    private static final Map<Integer, CheckStatus> integersToValues = stream(values())
            .collect(Collectors.toMap(CheckStatus::getValue, Function.identity()));

    CheckStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CheckStatus fromInteger(Integer value) {
        return integersToValues.get(value);
    }
}
