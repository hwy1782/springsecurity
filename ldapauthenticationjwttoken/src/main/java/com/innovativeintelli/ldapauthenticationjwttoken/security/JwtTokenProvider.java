package com.innovativeintelli.ldapauthenticationjwttoken.security;

import io.jsonwebtoken.*;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  private static final String AUTHORITIES_KEY = "auth";

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationInMs}")
  private int jwtExpirationInMs;

  public String createToken(Authentication authentication) {

    String authorities = authentication.getAuthorities().stream()
        .map(authority -> authority.getAuthority()).collect(Collectors.joining(","));

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime expirationDateTime = now.plus(this.jwtExpirationInMs, ChronoUnit.MILLIS);

    Date issueDate = Date.from(now.toInstant());
    Date expirationDate = Date.from(expirationDateTime.toInstant());

    String result = Jwts.builder().setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(SignatureAlgorithm.HS512, this.jwtSecret).setIssuedAt(issueDate)
        .setExpiration(expirationDate).compact();

    return result;
  }

  public Authentication getAuthentication(String token) {

    Claims claims = Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(token).getBody();

    Collection<? extends GrantedAuthority> authorities = Arrays
        .asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
        .map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

    User principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public boolean validateToken(String authToken) {

    try {
      Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.info("Invalid JWT signature: " + e.getMessage());
      logger.debug("Exception " + e.getMessage(), e);
      return false;
    }
  }

  public String getUsernameFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }


}