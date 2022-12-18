package com.example.dating_backend.jwt;

import com.example.dating_backend.user.User;
import com.example.dating_backend.user.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${spring.jwt.password}")
    private String secretKey;

    public String createToken(User user) {

        // auth 받기
        String authority = user.getUserRole().getAuthority();

        // 만료시간 설정
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofDays(1).toMillis());

        return Jwts.builder()
                .claim("auth", authority)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("tf") // 토큰 발급자(iss)
                .setAudience(user.getUserEmail()) // 토큰 대상자(aud)
                .setIssuedAt(now) // 발급시간(iat)
                .setExpiration(expiration) // 만료시간(exp)
                .setSubject("userToeken") // 토큰제목(subject)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
        } catch (ExpiredJwtException e) {
        } catch (UnsupportedJwtException e) {
        } catch (IllegalArgumentException e) {
        }
        return false;

    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetailsImpl principal = new UserDetailsImpl(claims.getAudience(), authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

}