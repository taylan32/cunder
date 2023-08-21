package com.example.cunder.security;

import com.example.cunder.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final TokenGenerator tokenGenerator;

    public JwtFilter(UserDetailsServiceImpl userDetailsService, TokenGenerator tokenGenerator) {
        this.userDetailsService = userDetailsService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        String username = null;
        //try {
            if(token != null) {
                username = tokenGenerator.verifyJWT(token).getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
       /* } catch (Exception exception) {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            Map<String, String> errors = new HashMap<>();
            errors.put("timestamp", new Date().toString());
            errors.put("message", exception.getMessage());
            errors.put("status", HttpStatus.UNAUTHORIZED.toString());
            errors.put("title", "Authorization error");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(errors));
        }*/
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }
}
