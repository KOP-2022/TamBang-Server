package com.example.tambang.configuration.security;

import com.example.tambang.configuration.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = jwtProvider.resolveTokenFromRequest(request);

        logger.info("authorization: " + authorization);

        if(authorization == null || !authorization.startsWith("Bearer ")){
            logger.error("authorization error");
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " removing
        String token = jwtProvider.BearerRemove(authorization);
        System.out.println("token = " + token);
        boolean isExpired = jwtProvider.isExpired(token);
        if(isExpired){
            logger.error("token is expired");
            filterChain.doFilter(request, response);
        }
        String username = jwtProvider.getUserEmail(token);
        System.out.println("username = " + username);
        Authentication authentication = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("end of jwt authorization filter");
        filterChain.doFilter(request, response);
    }
}