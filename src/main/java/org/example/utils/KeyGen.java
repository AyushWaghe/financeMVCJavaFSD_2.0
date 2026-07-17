package org.example.utils;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;

public class KeyGen {
    public static void main(String[] args) {
        String secret = Encoders.BASE64.encode(Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded());
        System.out.println(secret);
    }
}