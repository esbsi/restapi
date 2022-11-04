package be.abis.exercise.exception;

public class PasswordTooShortException extends Exception{

    public PasswordTooShortException() {
        super("Password too short.");
    }

    public PasswordTooShortException(String message) {
        super(message);
    }
}
