package org.launchcode.LiftoffRecipeProject.exception;

import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseWrapper<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ResponseWrapper<>("Resource not found", (List<UserDTO>) null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseWrapper<Object>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ResponseWrapper<>("An error occurred", (List<UserDTO>) null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(EntityValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseWrapper<Object>> handleEntityValidationException(EntityValidationException ex) {
        return new ResponseEntity<>(new ResponseWrapper<>("Validation errors", (List<UserDTO>) ex.getValidationErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseWrapper<Object>> handleDuplicateEntityException(DuplicateEntityException ex) {
        return new ResponseEntity<>(new ResponseWrapper<>(ex.getMessage(), (List<UserDTO>) null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseWrapper<Object>> handleOperationNotAllowedException(OperationNotAllowedException ex) {
        return new ResponseEntity<>(new ResponseWrapper<>(ex.getMessage(), (List<UserDTO>) null), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(UserAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseWrapper<Object>> handleUserAuthenticationException(UserAuthenticationException ex) {
        return new ResponseEntity<>(new ResponseWrapper<>(ex.getMessage(), (List<UserDTO>) null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseWrapper<Object>> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        return new ResponseEntity<>(new ResponseWrapper<>(ex.getMessage(), (List<UserDTO>) null), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseWrapper<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new ResponseWrapper<>(ex.getMessage(), (List<UserDTO>) null), HttpStatus.NOT_FOUND);
    }




}
