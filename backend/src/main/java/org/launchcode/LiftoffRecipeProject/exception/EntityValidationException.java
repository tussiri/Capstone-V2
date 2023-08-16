package org.launchcode.LiftoffRecipeProject.exception;

import java.util.Map;

public class EntityValidationException extends RuntimeException{
    private Map<String, String> validationErrors;

    public EntityValidationException(String message, Map<String, String> validationErrors){
        super(message);
        this.validationErrors=validationErrors;
    }
    public Map<String, String> getValidationErrors(){
        return validationErrors;
    }
}
