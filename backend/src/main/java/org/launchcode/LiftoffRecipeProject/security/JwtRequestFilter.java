package org.launchcode.LiftoffRecipeProject.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    private static final List<String> PUBLIC_ROUTES = Arrays.asList(
            "/",
            "/auth/register",
            "/auth/login",
            "/recipes",
            "/recipes/search",
            "/recipes/user/{userId}"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {




//        System.out.println("Executing JwtRequestFilter");

        final String requestTokenHeader = request.getHeader("Authorization");
//        System.out.println("Request token header: "+ requestTokenHeader);

        String username = null;
        String jwtToken = null;

        if (PUBLIC_ROUTES.contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                Integer userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
//                System.out.println("JWT Token has expired");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Your session has expired. Please log in again.");
                return;
            }
        } else {
            if (requestTokenHeader == null || requestTokenHeader.isEmpty()) {
                // Handle cases where there's no Authorization header at all
                // For example, you might want to log a different message or skip logging
            } else if (!PUBLIC_ROUTES.contains(request.getRequestURI())) {
                logger.warn("JWT Token does not begin with Bearer String");
            }
//            if (!request.getRequestURI().equals("/") &&
//                    !request.getRequestURI().startsWith("/auth") &&
//                    !request.getRequestURI().startsWith("/recipes") &&
//                    !request.getRequestURI().startsWith("/recipes/user/{userId}") &&
//                    !request.getRequestURI().startsWith("/recipes/search") &&
//                    !request.getRequestURI().startsWith("/review/recipes/{recipeId}/reviews")) {
//                logger.warn("JWT Token does not begin with Bearer String");
//            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
//        System.out.println("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        chain.doFilter(request, response);
    }


}