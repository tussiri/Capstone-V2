package org.launchcode.LiftoffRecipeProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.config.SecurityConfig;
import org.launchcode.LiftoffRecipeProject.controllers.AuthenticationController;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.security.JwtTokenUtil;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;


//    @Test
//    public void testRegisterUser() {
//        // Mock input data
//        UserDTO userDTO = new UserDTO();
//        // Set up any necessary mock behavior
//        when(userService.registerUser(userDTO)).thenReturn(userDTO);
//
//        // Make the request to the controller
//        ResponseEntity<ResponseWrapper<UserDTO>> response = authenticationController.register(userDTO);
//
//        // Verify the expected results
//        verify(userService, times(1)).registerUser(userDTO);
//        // Assert the response status and other properties as needed
//        assert response.getStatusCode() == HttpStatus.CREATED;
//    }

    @Test
    public void testRegisterUser() throws Exception {
        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setId(1);
        mockUserDTO.setFirstName("Testing");
        mockUserDTO.setLastName("MockTest");
        mockUserDTO.setEmail("testUser@example.com");
        mockUserDTO.setPassword("testPassword");
        mockUserDTO.setDateOfBirth(LocalDate.of(1991, 1, 1));
        mockUserDTO.setToken("mockToken");

        when(userService.registerUser(any(UserDTO.class))).thenReturn(mockUserDTO);
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockUserDTO))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Include the CSRF token
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.data.email", is("testUser@example.com")))
                .andExpect((ResultMatcher) jsonPath("$.message", is("User created successfully. JWT is: mockToken")));
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

    @Test
    public void testSuccessfulLogin() {

    }


}
