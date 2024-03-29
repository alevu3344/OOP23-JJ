package it.unibo.jetpackjoyride.utilities.exceptions;

/**
 * Exception thrown when a directory couldn't be created.
 * @author alessandro.valmori2@studio.unibo.it
 */
public class DirectoryCreationException extends Exception {
    /**
     * Defines the unique original class version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor to create a new DirectoryCreationException with the specified
     * detail message.
     *
     * @param message The message describing what and where something went wrong.
     */
    public DirectoryCreationException(final String message) {
        super(message);
    }
}
