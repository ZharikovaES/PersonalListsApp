package com.ZharikovaES.PersonalListsApp.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.security.auth.message.AuthException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtProvider {

    private final Algorithm accessAlgorithm;
    private final Algorithm refreshAlgorithm;
    private final Logger logger = Logger.getLogger(JwtProvider.class.getName());

    public JwtProvider(
            @Value("${jwt.secret.access}") String accessSecret,
            @Value("${jwt.secret.refresh}") String refreshSecret
    ) {
        this.accessAlgorithm = Algorithm.HMAC256(accessSecret);
        this.refreshAlgorithm = Algorithm.HMAC256(refreshSecret);
    }

    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(5, ChronoUnit.MINUTES)))
                .sign(accessAlgorithm);
    }

    public String generateRefreshToken(UserDetails user) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(30, ChronoUnit.DAYS)))
                .sign(refreshAlgorithm);
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessAlgorithm);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshAlgorithm);
    }

    public DecodedJWT getAccessClaims(String token) {
        return decodeToken(token, accessAlgorithm);
    }

    public DecodedJWT getRefreshClaims(String token) {
        return decodeToken(token, refreshAlgorithm);
    }

    private boolean validateToken(String token, Algorithm algorithm) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            logger.log(Level.SEVERE, "Invalid token: " + e.getMessage());
        }
        return false;
    }

    private DecodedJWT decodeToken(String token, Algorithm algorithm) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String getUsernameFromRefreshToken(String token) throws AuthException {
      try {
          return decodeToken(token, this.refreshAlgorithm).getSubject();
      } catch (JWTVerificationException e) {
          throw new AuthException("Невалидный refresh токен", e);
      }
  }
}
