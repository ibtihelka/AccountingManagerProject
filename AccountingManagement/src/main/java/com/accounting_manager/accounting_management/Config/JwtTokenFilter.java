package com.accounting_manager.accounting_management.Config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecret; // Your JWT secret key

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the JWT token from the Authorization header
        String jwtToken = extractJwtToken(request.getHeader("Authorization"));

        try {
            if (jwtToken != null && Jwts.parser().setSigningKey(jwtSecret).isSigned(jwtToken)) {
                // Parse the token and set up authentication
                Object thirdPartyObject = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(jwtToken)
                        .getBody()
                        .get("thirdParty");

                Map<String, Object> thirdPartyMap = (Map<String, Object>) thirdPartyObject;
                String username = thirdPartyMap.get("email").toString();
                List<GrantedAuthority> roles = AuthorityUtils.commaSeparatedStringToAuthorityList(
                        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody().get("role", String.class)
                );

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            // Handle token validation errors
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
