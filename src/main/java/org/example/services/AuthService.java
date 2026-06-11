package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.models.User;
import org.example.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor //This handles all the constructer initializations. Like below jwt service for final variables
public class AuthService {  //Login

    @Autowired
    AuthenticationManager  authenticationManager;

    @Autowired
    private final JwtService jwtService;

    public AuthResponse loginUser(AuthRequest authRequest) {
        try {
            Authentication authentication=authenticationManager.authenticate( //This calls load by username internally
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUseremail(),
                            authRequest.getPassword()
                    )
            );
            //If in-case the authentication fails then in that case the above authentication will throw error.

            User user=(User) authentication.getPrincipal();

            String token=jwtService.generateAccessToken(user);

            return new AuthResponse(true, user.getUserId(),user.getUseremail(), "Login successful",token);
        }catch (BadCredentialsException e){
            return new AuthResponse(false,null,"","Login Unsuccessful","");
        }
    }
}
