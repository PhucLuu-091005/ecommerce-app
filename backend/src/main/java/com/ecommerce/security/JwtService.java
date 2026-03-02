package com.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Service for generating, extracting username, validating JWT tokens for user authentication.
 */
@Service
public class JwtService {
  @Value("${jwt.secret}") String secretKey;
  @Value("${jwt.expiration-ms}") long jwtExpirationInMs;

  /**
   * Generates a JWT token for the given username + role.
   * @param userDetails the object contain user details.
   * @return a JWT token as a String
   */
  public String generateJwtToken(UserDetails userDetails) {
    return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
            .signWith(this.getSigningKey())
            .compact();
  }

  /**
   * Extracts the username from the given JWT token.
   * @param token the JWT token from which to extract the username
   * @return the username contained in the token
   * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
   */
  public String getUsernameFromJwtToken(String token) {
    return this.getClaimsFromJwtToken(token).getSubject();
  }

  /**
   * Validates the given JWT token by checking its username, role, and expiration.
   * @param token the JWT token to validate
   * @param userDetail the entity contains user detail
   * @return true if the token is valid and matches the expected username and role, false otherwise
   */
  public boolean validateJwtToken(String token, UserDetails userDetail) {
    if (userDetail == null) {
      return false;
    }

    String expectedUsername = userDetail.getUsername();

    return this.getUsernameFromJwtToken(token).equals(expectedUsername)
            && !this.isExpired(token);
  }

  /**
   * Checks if the given JWT token is expired by comparing its expiration date with the current date.
   * @param token the JWT token to check for expiration
   * @return true if the token is expired, false otherwise
   */
  private boolean isExpired(String token) {
    return this.getClaimsFromJwtToken(token).getExpiration()
            .before(new Date());
  }

  /**
   * Parses the given JWT token and extracts its claims (payload).
   * @param token the JWT token to parse
   * @return the claims contained in the token
   * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
   */
  private Claims getClaimsFromJwtToken(String token) {
    return Jwts.parser()
            .verifyWith(this.getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  /**
   * Retrieves the signing key used for generating and validating JWT tokens.
   * The key is derived from the secretKey property, which is injected from application properties.
   * @return the SecretKey used for signing JWT tokens
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
