package be.abis.exercise.exception;

public class PersonAlreadyExistsException extends Exception{

    public PersonAlreadyExistsException() {
        super("Person already exists.");
    }

    public PersonAlreadyExistsException(String message) {
        super(message);
    }
}
