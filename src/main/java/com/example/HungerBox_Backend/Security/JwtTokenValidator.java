package com.example.HungerBox_Backend.Security;

import com.example.HungerBox_Backend.Service.CustomerUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenValidator extends OncePerRequestFilter{

    @Autowired
    private  JwtProvider tokenGenerator;    // Service for generating and validating JWT tokens

    @Autowired
    private CustomerUserDetailService customerUserDetailService;    // Service for loading user details

    /**
     * Filter method to validate JWT token and set authentication in the security context.
     *
     * @param request       the HTTP request.
     * @param response      the HTTP response.
     * @param filterChain   the filter chain to pass the request and response to the next filter.
     * @throws ServletException if there is an error during the filter process.
     * @throws IOException      if there is an input/output error during the filter process.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract JWT token from the request
        String token = getJWTFromRequest(request);

        // If token is present and valid, set authentication in the security context
        if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)){

            String emailId = tokenGenerator.getEmailIdFromJwtToken(token);

            UserDetails userDetails = customerUserDetailService.loadUserByUsername(emailId);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());

            // Set details of the request for authentication
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts JWT token from the Authorization header of the request.
     *
     * @param request the HTTP request.
     * @return the JWT token or null if not found.
     */
    private String getJWTFromRequest(HttpServletRequest request){

        // Get the Authorization header
        String bearerToken = request.getHeader("Authoriation");

        // Check if the header is not null, starts with "Bearer " and extract the token
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());      // Extract token by removing "Bearer " prefix
        }

        return null;
    }
}