package com.example.vasooliDSA.GlobalException;

import com.example.vasooliDSA.DTO.DTOErrorResponse;
import com.example.vasooliDSA.Exceptions.*;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// *** IMPORTANT: Make sure this import is correct ***
import org.springframework.web.bind.MethodArgumentNotValidException; // <--- THIS IS THE CORRECT IMPORT
// If you have 'org.springframework.validation.method.MethodValidationException' imported, remove it.
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Objects; // Make sure to import this for Objects.requireNonNull

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<DTOErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, WebRequest request) {
        // Assuming DTOErrorResponse has a constructor like DTOErrorResponse(HttpStatus status, String message)
        // If it also has 'path' field, then add: request.getDescription(false).replace("uri=", "")
        DTOErrorResponse error = new DTOErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<DTOErrorResponse> handleUsernameAlreadyTakenException(
            UsernameAlreadyTakenException ex, WebRequest request) {
        DTOErrorResponse errorResponse = new DTOErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // *** THIS IS THE CRITICAL CORRECTION ***
    @ExceptionHandler(MethodArgumentNotValidException.class) // <-- EXCEPTION TYPE MUST BE MethodArgumentNotValidException
    public ResponseEntity<DTOErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request // <-- PARAMETER TYPE MUST ALSO BE MethodArgumentNotValidException
    ){
        // This part extracts the specific validation messages from the exception
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error ->Objects.requireNonNull(error.getDefaultMessage()))
                .findFirst() // Get only the first validation error message
                // You could use .collect(Collectors.joining("; ")) to get all messages, if preferred
                .orElse("Validation failed"); // Fallback if no specific field errors are found

        // Assuming DTOErrorResponse has a constructor like DTOErrorResponse(HttpStatus status, String message)
        // If it also has 'path' field, then add: request.getDescription(false).replace("uri=", "")
        DTOErrorResponse response = new DTOErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorMessage
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<DTOErrorResponse> handleInvalidCredentialException(
            InvalidCredentialException ex
    ){
        DTOErrorResponse errorResponse = new DTOErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ProblemAlreadyExistsException.class)
    public ResponseEntity<DTOErrorResponse> handleProblemAlreadyExistsException(
            ProblemAlreadyExistsException ex
    ){
        DTOErrorResponse response = new DTOErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ProblemMissingException.class)
    public ResponseEntity<DTOErrorResponse> handleProblemMissingException(
            ProblemMissingException ex
    ){
        DTOErrorResponse response = new DTOErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DTOErrorResponse> handleRuntimeException(
            RuntimeException ex
    ){
        DTOErrorResponse response = new DTOErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SolutionMissingException.class)
    public ResponseEntity<DTOErrorResponse> handleSolutionMissingException(
            SolutionMissingException ex
    ){
        DTOErrorResponse response = new DTOErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}