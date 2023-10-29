package array;

/**
 * Custom exception class for handling reflection-related errors.
 * <p>
 * This exception is thrown when an operation involving Java reflection fails.
 * It extends the {@link java.lang.Exception} class to provide additional
 * constructors for handling reflection-specific issues.
 * </p>
 */
public class ReflectionException extends Exception {

    /**
     * Default constructor that initializes the exception with a default message.
     * <p>
     * Calls the superclass constructor with no arguments.
     * </p>
     */
    public ReflectionException() {
        super();
    }

    /**
     * Constructor that initializes the exception with a custom message.
     * <p>
     * Calls the superclass constructor with a message argument.
     * </p>
     *
     * @param message The custom message to be associated with the exception.
     */
    public ReflectionException(String message) {
        super(message);
    }

    /**
     * Constructor that initializes the exception with a {@link Throwable} cause.
     * <p>
     * Calls the superclass constructor with a {@link Throwable} argument.
     * </p>
     *
     * @param cause The original cause of the exception.
     */
    public ReflectionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor that initializes the exception with a custom message and a {@link Throwable} cause.
     * <p>
     * Calls the superclass constructor with both a message and a {@link Throwable} argument.
     * </p>
     *
     * @param message The custom message to be associated with the exception.
     * @param cause   The original cause of the exception.
     */
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
