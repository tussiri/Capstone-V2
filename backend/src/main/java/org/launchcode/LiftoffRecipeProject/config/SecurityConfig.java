package org.launchcode.LiftoffRecipeProject.config;

import org.launchcode.LiftoffRecipeProject.security.JwtAuthenticationEntryPoint;
import org.launchcode.LiftoffRecipeProject.security.JwtRequestFilter;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    private AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(),
                        new AntPathRequestMatcher("/auth/register"),
                        new AntPathRequestMatcher("/auth/login"),
                        new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/recipes", "GET"),
                        new AntPathRequestMatcher("/recipes/{recipeId}", "GET"),
                        new AntPathRequestMatcher("/recipes/user/{userId}", "GET"),
                        new AntPathRequestMatcher("/recipes/ingredient/{ingredientName}", "GET"),
                        new AntPathRequestMatcher("/recipes/{recipeId}", "GET"),
                        new AntPathRequestMatcher("/review/recipes/{recipeId}/reviews", "GET"),
                        new AntPathRequestMatcher("/recipes/search", "GET"),
                        new AntPathRequestMatcher("/recipes/random", "GET"),
                        new AntPathRequestMatcher("/review", "GET"),
                        new AntPathRequestMatcher("/review/recipe/{recipeId}", "GET"),
                        new AntPathRequestMatcher("/review/{id}", "GET"),
                        new AntPathRequestMatcher("/users/{userId}/favorites", "GET"),
                        new AntPathRequestMatcher("/users/{userId}/favorites/{recipeId}", "POST"),
                        new AntPathRequestMatcher("/users/{userId}/favorites/{recipeId}", "DELETE"))
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}