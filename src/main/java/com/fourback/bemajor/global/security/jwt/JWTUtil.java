package com.fourback.bemajor.global.security.jwt;

import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {
    private final SecretKey secretKey;
    private final RedisService redisService;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, RedisService redisService) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.redisService = redisService;
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public List<Pair<String, String>> createTokens(Long userId, String role) {
        String access = "access";
        String refresh = "refresh";
        long expiredTime = 86400000L;
        List<Pair<String, String>> tokens = new ArrayList<>(2);
        tokens.add(Pair.of(access, this.createToken(access, userId, role, 600000L)));
        String refreshToken = this.createToken(refresh, userId, role, expiredTime);
        tokens.add(Pair.of(refresh, refreshToken));
        redisService.setValueWithExpiredTime(RedisKeyPrefixEnum.REFRESH, userId, refreshToken, expiredTime);
        return tokens;
    }

    private String createToken(String category, Long userId, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category).claim("userId", userId)
                .claim("role", role).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(secretKey)
                .compact();
    }
}
