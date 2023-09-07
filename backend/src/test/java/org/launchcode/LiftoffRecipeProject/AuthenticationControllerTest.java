package org.launchcode.LiftoffRecipeProject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.controllers.AuthenticationController;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.security.JwtAuthenticationEntryPoint;
import org.launchcode.LiftoffRecipeProject.security.JwtTokenUtil;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.fail;


import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthenticationController(jwtTokenUtil, customUserDetailsService, authenticationManager, userRepository, userService, bCryptPasswordEncoder, jwtTokenUtil)).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("password");
        userDTO.setFirstName("First");
        userDTO.setLastName("Last");
        userDTO.setDateOfBirth(LocalDate.parse("01-01-1991", DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        userDTO.setToken("someToken");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonUserDto = objectMapper.writeValueAsString(userDTO);

        when(userService.registerUser(any(UserDTO.class))).thenReturn(userDTO);

        // When
        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserDto))
                .andExpect(status().isCreated())
                .andReturn();

        // Deserialize response
        String jsonResponse = result.getResponse().getContentAsString();
        ResponseWrapper<UserDTO> responseWrapper = objectMapper.readValue(jsonResponse, new TypeReference<ResponseWrapper<UserDTO>>() {
        });

        // Then
        assertEquals("User created successfully. JWT is: " + userDTO.getToken(), responseWrapper.getMessage());
        assertEquals(userDTO, responseWrapper.getData());
    }

    @Test
    public void testLoginUser() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@test.com");
        loginDTO.setPassword("password");

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("password");
        userDTO.setFirstName("First");
        userDTO.setLastName("Last");
        userDTO.setToken("someToken");

        // Convert LoginDTO to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginDto = objectMapper.writeValueAsString(loginDTO);

        when(userService.loginUser(any(LoginDTO.class))).thenReturn(userDTO);

        // When
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDto))
                .andExpect(status().isOk())
                .andReturn();

        // Deserialize response
        String jsonResponse = result.getResponse().getContentAsString();
        ResponseWrapper<UserDTO> responseWrapper = objectMapper.readValue(jsonResponse, new TypeReference<ResponseWrapper<UserDTO>>() {
        });

        // Then
        assertEquals("Login successful. JWT is: " + userDTO.getToken(), responseWrapper.getMessage());
        assertEquals(userDTO, responseWrapper.getData());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testRefreshToken_Success() throws Exception {
        String expiredToken = "expiredToken";
        String newToken = "newToken";

        when(jwtTokenUtil.canRefreshToken(anyString())).thenReturn(true);
        when(jwtTokenUtil.generateTokenFromExpiredToken(anyString())).thenReturn(newToken);

        mockMvc.perform(post("/auth/token/refresh")
                        .header("Authorization", "Bearer " + expiredToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(jwtTokenUtil, times(1)).canRefreshToken(expiredToken);
        verify(jwtTokenUtil, times(1)).generateTokenFromExpiredToken(expiredToken);
    }

    @Test
    public void testRegisterUser_Failure() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(null);
        userDTO.setPassword(null);
        userDTO.setFirstName("First");
        userDTO.setLastName("Last");
        userDTO.setDateOfBirth(LocalDate.parse("01-01-1991", DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        userDTO.setToken("someToken");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonUserDto = objectMapper.writeValueAsString(userDTO);

        when(userService.registerUser(any(UserDTO.class))).thenReturn(userDTO);

        when(userService.registerUser(any(UserDTO.class))).thenThrow(new RuntimeException("Registration failed"));

        // When & Then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidTokenFormat() throws Exception {
        // Given
        String invalidToken = "invalidToken";

        // When & Then
        mockMvc.perform(post("/auth/token/refresh")
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testTokenRefresh_Failure() throws Exception {
        // Given
        String expiredToken = "expiredToken";

        when(jwtTokenUtil.canRefreshToken(anyString())).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/auth/token/refresh")
                        .header("Authorization", "Bearer " + expiredToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }



}


