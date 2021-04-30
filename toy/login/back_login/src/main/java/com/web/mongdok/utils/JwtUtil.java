package com.web.mongdok.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.web.mongdok.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    public final static long TOKEN_VALIDATION_SECOND = 1000 * 60 * 60 * 12; // access token 유효기간: 12시간
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 24 * 30; // refresh token 유효기간: 30일

    final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰이 유효한 토큰인지 검사한 후, 토큰에 담긴 Payload 값을 가져온다.
    public Claims extractAllClaims(String token) throws ExpiredJwtException {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey(SECRET_KEY))
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
    	
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser()
            			.setSigningKey(SECRET_KEY.getBytes())
            			.parseClaimsJws(token);
        
        } catch (final Exception e) {
            throw new RuntimeException();
        }

        System.out.println("claims : {}" + claims);
        // Claims는 Map의 구현체이다.
        return claims.getBody();
    }

    // 추출한 Payload로부터 email을 가져온다.
    public String getEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    // 토큰이 만료됐는지 안됐는지 확인.
    public Boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // Access/Refresh Token 생성
    public String generateToken(User user) {
        return doGenerateToken(user, TOKEN_VALIDATION_SECOND);
    }

    public String generateRefreshToken(User user) {
        return doGenerateToken(user, REFRESH_TOKEN_VALIDATION_SECOND);
    }

    // : 토큰을 생성, 페이로드에 담길 값은 email (임시)
    public String doGenerateToken(User user, long expireTime) {
    	SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256 ;
    	
        Claims claims = Jwts.claims();
        claims.put("email", user.getEmail());

        String jwt = null;
        try {
        	
	        jwt = Jwts.builder()
	                .setClaims(claims)
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
	                .signWith(signatureAlgorithm, SECRET_KEY.getBytes("UTF-8"))
	                .compact();

        } catch (Exception e) {
        	e.printStackTrace();
        }
        return jwt;
    }

    public Boolean validateToken(String token, User user) {
        final String username = getEmail(token);

        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

}