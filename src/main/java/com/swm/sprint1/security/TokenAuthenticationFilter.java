package com.swm.sprint1.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swm.sprint1.exception.CustomJwtException;
import com.swm.sprint1.exception.ResourceNotFoundException;
import com.swm.sprint1.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private final CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = tokenProvider.getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                tokenProvider.validateAccessToken(jwt);
                authenticateUser(request, jwt);
            }
        } catch (CustomJwtException exception) {
            logger.error(exception.getMessage());
            createResponse(response, exception.getErrorCode(), exception.getMessage());
            return;
        } catch (ResourceNotFoundException exception) {
            logger.error(exception.getMessage());
            createResponse(response, exception.getErrorCode(), exception.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(HttpServletRequest request, String jwt) {
        UserDetails userDetails = customUserDetailsService.loadUserByJwt(jwt);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void createResponse(HttpServletResponse response, String errorCode, String message) throws IOException {
        ApiResponse apiResponse = new ApiResponse(false, errorCode, message);
        String responseString = objectMapper.writeValueAsString(apiResponse);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(responseString);
    }

}