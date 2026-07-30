package com.interviewcoach.project.security;


import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.interviewcoach.project.models.User;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Map; 
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey ; 
    
    
    public String generateToken(User user) {
        Map<String, Object> myClaims = new HashMap<>() ; 
        myClaims.put("role", user.getUserRole()) ;


        return Jwts.builder()
                .claims(myClaims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignInKey())
                .compact();

    }
    public String getRole(String token) {
        Claims myClaims = extractAllClaims(token);
        return myClaims.get("role").toString() ;
    }

    public String getUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token){
        try{
        return !isTokenExpired(token);
        }
        catch(ExpiredJwtException ex) {
            return false ; 
        }
    }

        private SecretKey getSignInKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token
    ) {
        
        return extractExpiration(token)
                .before(new Date());
        
    
                }            }
