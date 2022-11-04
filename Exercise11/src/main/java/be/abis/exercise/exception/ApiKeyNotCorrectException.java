package be.abis.exercise.exception;

public class ApiKeyNotCorrectException extends Exception{

    public ApiKeyNotCorrectException() {
    }

    public ApiKeyNotCorrectException(String message) {
        super(message);
    }

}
