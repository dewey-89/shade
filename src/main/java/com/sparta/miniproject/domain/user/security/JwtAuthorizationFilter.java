package com.sparta.miniproject.domain.user.security;

import com.sparta.miniproject.domain.user.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {// OncePerRequestFilter : 한번만 실행되도록 필터링

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) { // JwtAuthorizationFilter 생성자 // JwtUtil, UserDetailsServiceImpl 주입
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // JWT 검증 및 인가
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        // doFilterInternal : 필터링을 위한 메소드// HttpServletRequest, HttpServletResponse, FilterChain 주입// ServletException, IOException 예외처리

        String tokenValue = jwtUtil.getJwtFromHeader(req);// 헤더에서 JWT 토큰을 받아옴

        if (StringUtils.hasText(tokenValue)) {// 토큰이 존재할 경우

            if (!jwtUtil.validateToken(tokenValue)) {// 토큰이 유효하지 않을 경우
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);// 토큰에서 사용자 정보를 받아옴// Claims : JWT의 payload 부분을 담는 클래스// getUserInfoFromToken : 토큰에서 사용자 정보를 받아오는 메소드

            try {
                setAuthentication(info.getSubject());// 인증 처리// getSubject : 토큰에서 사용자 식별자값(ID)을 가져오는 메소드// setAuthentication : 인증 처리를 위한 메소드
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);// 필터링 체인에 현재 필터를 추가 // doFilter : 필터링을 위한 메소드 // HttpServletRequest, HttpServletResponse, FilterChain 주입 //
    }

    // 인증 처리
    public void setAuthentication(String username) {// 인증 처리를 위한 메소드 // String username 주입
        SecurityContext context = SecurityContextHolder.createEmptyContext();// SecurityContext : 인증 객체를 담는 클래스 // createEmptyContext : 인증 객체를 담는 메소드
        Authentication authentication = createAuthentication(username);// 인증 객체 생성 // String username 주입
        context.setAuthentication(authentication);// 인증 객체를 SecurityContext에 담음 // Authentication authentication 주입

        SecurityContextHolder.setContext(context); // SecurityContext를 SecurityContextHolder에 담음 // SecurityContext context 주입
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
