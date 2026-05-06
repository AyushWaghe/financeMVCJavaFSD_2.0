package org.example.services;

import org.example.dao.UserRepository;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.exceptions.UserExistsException;
import org.example.models.User;
import org.example.models.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {  //Login

    @Autowired
    AuthenticationManager  authenticationManager;

    public AuthResponse loginUser(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate( //This calls load by username internally
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUseremail(),
                            authRequest.getPassword()
                    )
            );

            return new AuthResponse(true,null, authRequest.getUseremail(), "Login successful");
        }catch (BadCredentialsException e){
            return new AuthResponse(false,null,"","Login Unsuccessful");
        }
    }
}
