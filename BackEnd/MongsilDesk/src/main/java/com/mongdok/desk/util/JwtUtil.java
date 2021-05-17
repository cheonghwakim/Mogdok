package com.mongdok.desk.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

/**
 * author: pinest94
 * since: 2021-05-07
 */

@Slf4j
@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
    }

    /**
     * 이름으로 Jwt Token을 생성한다.
     */
//    public String generateToken(String userId, String userName) {
//        Date now = new Date();
//        return Jwts.builder()
//                .claim("userId", userId)
//                .claim("userName", userName)
//                .setIssuedAt(now) // 토큰 발행일자
//                .signWith( Keys.hmacShaKeyFor(secretKey.getBytes()),SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String getUserName(String token) {
//        Claims claims = getClaims(token);
//        return claims.get("userName", String.class);
//    }
//
//    public String getUserId(String token) {
//        Claims claims = getClaims(token);
//        return claims.get("userId", String.class);
//    }


    /**
     * 토큰의 유효성 검증 메서드
     * */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (SignatureException e) {
            log.error("Invalid JWT Signature");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
