package com.ecommerce.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException e) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
    errorResponse.put("message", "Invalid username or password, please try again :D");
    // Add error code if needed
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<Map<String, Object>> handleExpiredJwtException(ExpiredJwtException e) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
    errorResponse.put("message", "Token expired, try login again :D");
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<Map<String, Object>> handleSignatureException(SignatureException e) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
    errorResponse.put("message", "Invalid Token :(((");
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleOverallException(Exception e) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorResponse.put("message", "Cloudflare error, please try again :)))))");
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
