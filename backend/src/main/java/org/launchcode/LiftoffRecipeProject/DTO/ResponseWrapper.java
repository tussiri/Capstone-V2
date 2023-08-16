package org.launchcode.LiftoffRecipeProject.DTO;

import java.util.List;

 /*
 * A generic wrapper for API responses to provide a consistent structure for all responses.
 * Fields:
 * status: The HTTP status code of the response.
 * message: A descriptive message associated with the response.
 * data: The actual data payload of the response, of type T.
 * token: A token that might be used for session management or authentication purposes.
 * */
public class ResponseWrapper<T> {

    private int status;
    private String message;
    private T data;
    private String token;

    public ResponseWrapper(String userRetrievedSuccessfully, UserDTO userDTO) {
    }

    public ResponseWrapper(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.token= token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
