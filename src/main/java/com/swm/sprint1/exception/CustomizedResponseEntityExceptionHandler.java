package com.swm.sprint1.exception;

import com.swm.sprint1.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage());
        ApiResponse response = new ApiResponse(false, "500", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class, NotSupportedExtension.class, MissingServletRequestParameterException.class})
    public final ResponseEntity<ApiResponse> handleConstraintViolationExceptions(Exception ex) {
        log.error(ex.getMessage());
        ApiResponse response = new ApiResponse(false, "102", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestParamException.class)
    public final ResponseEntity<ApiResponse> handleRequestParamExceptions(Exception ex) {
        log.error(ex.getMessage());
        RequestParamException exception = (RequestParamException) ex;
        ApiResponse response = new ApiResponse(false, exception.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ApiResponse> handleResourceNotFoundExceptions(Exception ex) {
        log.error(ex.getMessage());
        ResourceNotFoundException exception = (ResourceNotFoundException) ex;
        ApiResponse response = new ApiResponse(false, exception.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestaurantLessThan7Exception.class)
    public final ResponseEntity<ApiResponse> handleRestaurantLessThan7Exceptions(Exception ex) {
        log.error(ex.getMessage());
        ApiResponse response = new ApiResponse(false, "211", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtException.class)
    public final ResponseEntity<ApiResponse> handleJwtExceptions(Exception ex) {
        ApiResponse response;
        if (ex.getClass().equals(ExpiredJwtException.class)) {
            log.error("Expired JWT token");
            response = new ApiResponse(false, "400", "Expired JWT token");
        } else {
            log.error(ex.getMessage());
            response = new ApiResponse(false, "401", "Invalid JWT token");
        }

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomJwtException.class)
    public final ResponseEntity<ApiResponse> handleCustomJwtExceptions(Exception ex) {
        log.error(ex.getMessage());
        CustomJwtException exception = (CustomJwtException) ex;
        ApiResponse response = new ApiResponse(false, exception.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OAuth2AuthenticationProcessingException.class)
    public final ResponseEntity<ApiResponse> handleOAuth2AuthenticationProcessingExceptions(Exception ex) {
        log.error(ex.getMessage());
        ApiResponse response = new ApiResponse(false, ex.getMessage(), "403");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
