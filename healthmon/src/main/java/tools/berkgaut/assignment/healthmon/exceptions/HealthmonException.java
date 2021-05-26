package tools.berkgaut.assignment.healthmon.exceptions;

// TODO: add exception to response mapping
public class HealthmonException extends Exception {
    private final int status;
    private final String message;

    public HealthmonException(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() { return message; }
}
