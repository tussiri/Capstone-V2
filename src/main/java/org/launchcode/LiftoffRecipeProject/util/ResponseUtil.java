package org.launchcode.LiftoffRecipeProject.util;

import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static <T> ResponseEntity<ResponseWrapper<T>> wrapResponse(T data, HttpStatus status, String message) {
        ResponseWrapper<T> response = new ResponseWrapper<>(status.value(), message, data);
        return new ResponseEntity<>(response, status);
    }
}