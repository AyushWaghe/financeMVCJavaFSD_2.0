package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserRepository;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.AuthResult;
import org.example.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor //This handles all the constructor initializations. Like below jwt service for final variables
public class AuthService {  //Login

    private final AuthenticationManager  authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthResult loginUser(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate( //If crednetials wrong then this will throw bad crednetials exception
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUseremail(),
                            authRequest.getPassword()
                    )
            );

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtService.generateAccessToken(customUserDetails);

            return new AuthResult(new AuthResponse(
                    true,
                    customUserDetails.getUserId(),
                    customUserDetails.getUsername(),
                    "Login successful"
            ),token);


        }catch (BadCredentialsException e){
            return new AuthResult(new AuthResponse(false,null,null,"Login unsuccessful"),null);
        }
    }
}
