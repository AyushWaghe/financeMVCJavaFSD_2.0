package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserRepository;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.AuthResult;
import org.example.models.User;
import org.example.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor //This handles all the constructer initializations. Like below jwt service for final variables
public class AuthService {  //Login

    @Autowired
    AuthenticationManager  authenticationManager;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserRepository userRepository;

    public AuthResult loginUser(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUseremail(),
                            authRequest.getPassword()
                    )
            );

            org.springframework.security.core.userdetails.User userDetails =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            User user = userRepository.findByUseremail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtService.generateAccessToken(user);

            return new AuthResult(new AuthResponse(
                    true,
                    user.getUserId(),
                    user.getUseremail(),
                    "Login successful"
            ),token);


        }catch (BadCredentialsException e){
            return new AuthResult(new AuthResponse(false,null,null,"Login unsuccessful"),null);
        }
    }
}
