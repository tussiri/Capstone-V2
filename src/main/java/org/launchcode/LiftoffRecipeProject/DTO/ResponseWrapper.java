package org.launchcode.LiftoffRecipeProject.DTO;

import java.util.List;

public class ResponseWrapper<T> {

    private int status;
    private String message;
    private T data;

    public ResponseWrapper(String userRetrievedSuccessfully, UserDTO userDTO) {
    }

    public ResponseWrapper(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseWrapper(String usersRetrievedSuccessfully, List<UserDTO> userDTOs) {
    }

    public ResponseWrapper(int value, Object o) {
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
