package org.launchcode.LiftoffRecipeProject;

import org.junit.jupiter.api.Test;
import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.controllers.AuthenticationController;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AuthenticationControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void testRegister() {
        // Mock input data
        UserDTO userDTO = new UserDTO();
        // Set up any necessary mock behavior
        when(userService.registerUser(userDTO)).thenReturn(userDTO);

        // Make the request to the controller
        ResponseEntity<ResponseWrapper<UserDTO>> response = authenticationController.register(userDTO);

        // Verify the expected results
        verify(userService, times(1)).registerUser(userDTO);
        // Assert the response status and other properties as needed
        assert response.getStatusCode() == HttpStatus.CREATED;
    }

    @Test
    public void testLoginUser() throws Exception {
        // Mock input data
        LoginDTO loginDTO = new LoginDTO();
        UserDTO userDTO = new UserDTO();
        when(userService.loginUser(loginDTO)).thenReturn(userDTO);
        ResponseEntity<ResponseWrapper<UserDTO>> response = authenticationController.loginUser(loginDTO);
        verify(userService, times(1)).loginUser(loginDTO);
        assert response.getStatusCode() == HttpStatus.OK;
    }
}
