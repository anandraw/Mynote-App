package org.anand.mynoteapp.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.exception.JwtTokenExpiredException;
import org.anand.mynoteapp.service.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private String secretKey = "";

    public JwtServiceImpl() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("role", user.getRoles());
        claims.put("status", user.getStatus().isActive());

        String token = Jwts.builder().claims().add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 *60* 10))
                .and()
                .signWith(getKey())
                .compact();

        return token;
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllclaims(token);
        return claims.getSubject();
    }

    private Claims extractAllclaims(String token) {
        try{
            return Jwts.parser().verifyWith(decrptKey(secretKey))
                    .build().parseSignedClaims(token).getPayload();
        }catch (ExpiredJwtException e){
            throw new JwtTokenExpiredException("Token Is Expired");
        }catch (JwtException e){
            throw new JwtTokenExpiredException("Invalid Jwt Token");
        }catch (Exception e){
            throw e;
        }
    }

    private SecretKey decrptKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public boolean validateToken(String token, UserDetails user) {
        String username = extractUsername(token);
        Boolean isExpired=isTokenExpried(token);
        if (username.equalsIgnoreCase(user.getUsername()) && !isExpired){
            return true;
        }
        return false;
    }

    private Boolean isTokenExpried(String token) {
        Claims claims = extractAllclaims(token);
        Date expiration = claims.getExpiration();
        // 20th jun - today - expir- 21th jun
        return expiration.before(new Date());
    }
}
