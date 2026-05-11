package org.example.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.models.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    private String secretKey="sdfsdfsd435dssssssssssssssssffsfdfsfs";

    private SecretKey getSecretKey(){ //This method will give the secret key
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getUseremail().toString())
                .claim("userId",user.getUserId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10)) //Valid for 10mins
                .signWith(getSecretKey()) //Here we are passing the above secret key
                .compact();
    }
}

