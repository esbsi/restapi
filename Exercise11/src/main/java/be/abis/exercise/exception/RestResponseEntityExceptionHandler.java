package be.abis.exercise.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

/*    @ExceptionHandler(value = PersonNotFoundException.class)
    protected ResponseEntity<? extends Object> handlePersonNotFound(PersonNotFoundException pnfe, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError apiError = new ApiError("Person not found.", status.value(), pnfe.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(apiError, responseHeaders, status);
    }
*/

    @ExceptionHandler(value = PersonAlreadyExistsException.class)
    protected ResponseEntity<? extends Object> handlePersonAlreadyExists(PersonAlreadyExistsException e, WebRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        ApiError apiError = new ApiError("Person already exists.", status.value(), e.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<>(apiError, responseHeaders, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request){
        ApiError apiError = new ApiError("invalid arguments", status.value(), e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<ValidationError> validationErrors = apiError.getInvalidParams();
        for (FieldError fieldError : fieldErrors){
            ValidationError validationError = new ValidationError();
            validationError.setName(fieldError.getField());
            validationError.setReason(fieldError.getDefaultMessage());
            validationErrors.add(validationError);
        } headers.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<Object>(apiError, headers, status);
    }

    @ExceptionHandler(value = ApiKeyNotCorrectException.class)
    protected ResponseEntity<Object> handleApiKeyNotCorrect(ApiKeyNotCorrectException e, WebRequest request){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError("unauthorized", status.value(), e.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<>(apiError, responseHeaders, status);
    }



/*    @ExceptionHandler(value = PersonAlreadyExistsException.class)
    protected ResponseEntity<? extends Object> handlePasswordShort(PasswordTooShortException e, WebRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiError apiError = new ApiError("Password Too Short.", status.value(), e.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<>(apiError, responseHeaders, status);
    }
*/

}
