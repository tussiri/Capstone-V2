package org.launchcode.LiftoffRecipeProject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.launchcode.LiftoffRecipeProject.security.JwtTokenUtil;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Test
    public void testGenerateToken() {
        // Create a UserDetails instance with mock data
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());

        // Generate the token
        String token = jwtTokenUtil.generateToken(userDetails);

        // Assert that the token is not null
        Assertions.assertNotNull(token);

        // Print the token for manual validation
        System.out.println("Generated Token: " + token);

        // Validate the token manually using an online JWT validation tool or decode it
        // to ensure that it contains the expected claims and expiration.
        // You can copy the generated token and validate it using a tool like https://jwt.io/.
        // Make sure the claims and expiration are correct.
    }

}
