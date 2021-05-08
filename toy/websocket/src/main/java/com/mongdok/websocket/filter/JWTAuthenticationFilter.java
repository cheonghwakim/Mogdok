//package com.mongdok.websocket.filter;
//
//import com.mongdok.websocket.util.JWTUtil;
//import io.jsonwebtoken.Claims;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * author: pinest94
// * since: 2021-05-07
// */
//public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
//
//    private JWTUtil jwtUtil;
//
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
//        super(authenticationManager);
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain chain) throws IOException, ServletException {
//
//        String token = request.getHeader("token");
//        logger.info("token : " + token);
//
//        Claims claims = null;
//        if(token != null && token.length() > 6) { // token이 있는 경우
//            claims = jwtUtil.getClaims(token.substring("Bearer ".length()));
//        }
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(claims, null);
//
//        if(authentication != null) {
//            SecurityContext context = SecurityContextHolder.getContext();
//            context.setAuthentication(authentication);
//        }
//
//        chain.doFilter(request, response);
//    }
//
//}
