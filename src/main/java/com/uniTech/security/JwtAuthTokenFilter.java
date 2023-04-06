package com.uniTech.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniTech.models.ResponseData;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private JwtProvider tokenProvider;
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/uni-tech/api/v1/auth/")
                ||request.getRequestURI().startsWith("/swagger")
                ||request.getRequestURI().startsWith("/v2/api-docs")){
            filterChain.doFilter(request, response);
        } else{
            try {
                String jwt = getJwt(request);
                if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                    String username = tokenProvider.getUserNameFromJwtToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                } else {
                    log.error("Your token is expired or not found");
                    returnResponseBody("Your token is expired or not found", response);
                }
            } catch (Exception e) {
                log.error("Can NOT set user authentication -> Message: {0}", e);
                returnResponseBody("Can NOT set user authentication", response);
            }
        }
    }

    private void returnResponseBody(String message, @NonNull HttpServletResponse response) throws IOException {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", message);
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), ResponseData.error(responseBody, UNAUTHORIZED));
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}