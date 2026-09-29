package org.example.dto;

public record AuthResult(
   AuthResponse authResponse,
   String jwt
) {}
