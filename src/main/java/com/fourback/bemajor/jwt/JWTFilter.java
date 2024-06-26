package com.fourback.bemajor.jwt;

import com.fourback.bemajor.domain.CustomUserDetails;
import com.fourback.bemajor.dto.UserAuthDto;
import com.fourback.bemajor.exception.AccessTokenExpiredException;
import com.fourback.bemajor.exception.InvalidLoginTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter
{
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("access");
        if(accessToken == null){
            filterChain.doFilter(request, response);
            return;
        }
        if (jwtUtil.isExpired(accessToken)) {
            //response status code
            throw new AccessTokenExpiredException(6, "Access Token Expired", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        UserAuthDto userAuthDto = new UserAuthDto(username, role);
        CustomUserDetails customOAuth2User = new CustomUserDetails(userAuthDto);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
