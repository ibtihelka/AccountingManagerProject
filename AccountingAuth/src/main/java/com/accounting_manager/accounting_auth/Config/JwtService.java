package com.accounting_manager.accounting_auth.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class JwtService {

    private String secretKey;
    private long jwtExpiration;

    private final Cache<String, String> tokenCache;

    //read jwt secret key and expiration values from application properties
    public JwtService(@Value("${application.security.jwt.expiration-time-in-minutes}") long jwtExpiration,
                      @Value("${application.security.jwt.secret-key}") String secretKey) {

        this.jwtExpiration = jwtExpiration;
        this.secretKey = secretKey;
        this.tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(jwtExpiration, TimeUnit.MINUTES)
                .build();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        HashMap<String,Object> extraClaims = new HashMap<>();
        extraClaims.put("thirdParty",userDetails);
        extraClaims.put("role",userDetails.getAuthorities().iterator().next().getAuthority());
        String token = buildToken(extraClaims, jwtExpiration);
        storeToken(token);
        return token;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //converting expiration time from minutes to milliseconds
                .setExpiration(new Date(System.currentTimeMillis() + (expiration * 60000)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenValid(String token){
        return tokenCache.getIfPresent(token) == null ? false : true;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //store jwt tokens in caffeine cache, key token/valuue token
    private void storeToken(String token) {
        tokenCache.put(token, token);
    }

    public void removeToken(String token) {
        tokenCache.invalidate(token);
    }
}