package com.webchat.security.jwt;

import com.webchat.config.props.AppProperties;
import com.webchat.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private static final String AUTH_AUDIENCE = "auth";
    private static final String MAIL_AUDIENCE = "mail";


    private final UserDetailsService userDetailsService;
    private final AppProperties appProperties;
    private String secret;

    @Autowired
    public JwtTokenProviderImpl(UserDetailsService userDetailsService, AppProperties appProperties) {
        this.userDetailsService = userDetailsService;
        this.appProperties = appProperties;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(appProperties.getSecret().getBytes());
    }

    @Override
    public String createEmailToken(String username) {
        Claims claims = Jwts.claims()
                .setSubject(username)
                .setAudience(MAIL_AUDIENCE);

        return createToken(claims);
    }

    @Override
    public String createAuthToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims()
                .setSubject(username)
                .setAudience(AUTH_AUDIENCE);
        claims.put("roles", getRoleNames(roles));

        return createToken(claims);
    }

    private String createToken(Claims claims) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + appProperties.getTokenExpiration());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Override
    public boolean validateEmailToken(String token) {
        return validateToken(token, MAIL_AUDIENCE);
    }

    @Override
    public boolean validateAuthToken(String token) {
        return validateToken(token, AUTH_AUDIENCE);
    }

    private boolean validateToken(String token, String audience) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            return !claims.getExpiration().before(new Date()) && claims.getAudience().equals(audience);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public String resolveToken(StompHeaderAccessor stompHeaderAccessor) {
        List<String> headers = stompHeaderAccessor.getNativeHeader("Authorization");
        if (headers == null || headers.size() == 0) {
            return null;
        }

        String bearerToken = headers.get(0);
        return bearerToken.startsWith("Bearer ")
                ? bearerToken.substring(7)
                : null;
    }

    private List<String> getRoleNames(List<Role> userRoles) {
        return userRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}