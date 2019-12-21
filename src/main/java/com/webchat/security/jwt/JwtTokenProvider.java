package com.webchat.security.jwt;

import com.webchat.model.Role;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface JwtTokenProvider {
    String createEmailToken(String username);

    String createAuthToken(String username, List<Role> roles);
    
    String resolveToken(HttpServletRequest request);

    String resolveToken(StompHeaderAccessor stompHeaderAccessor);

    boolean validateEmailToken(String token);

    boolean validateAuthToken(String token);

    Authentication getAuthentication(String token);
}
