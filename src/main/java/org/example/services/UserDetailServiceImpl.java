package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserRepository;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.exceptions.UserExistsException;
import org.example.models.UserDetail;
import org.example.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService { //Signup

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;  //This bean is injected/autowired from the spring security config which we have created

    @Autowired
    private final JwtService jwtService;


    public AuthResponse registerUser(AuthRequest authRequest) {

        String useremail= authRequest.getUseremail();

        if(userRepository.existsByUseremail(useremail)){
            throw new UserExistsException("User with this email already exists");
        }

        String encodedPassword =passwordEncoder.encode(authRequest.getPassword());
        UserDetail userDetail=new UserDetail();
        org.example.models.User user=new org.example.models.User(encodedPassword, useremail, userDetail);
        userDetail.setUser(user);
        org.example.models.User savedUser=userRepository.save(user);
        String token=jwtService.generateAccessToken(user);

        return new AuthResponse(true,savedUser.getUserId(),useremail,"User saved successfully",token);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<org.example.models.User> user=userRepository.findByUseremail(username);
        if (!user.isEmpty()){
            UserDetails userDetails=org.springframework.security.core.userdetails.User.builder()
                    .username(user.get().getUseremail())
                    .password(user.get().getPassword())
                    .build();   //Here we are building user details
            return userDetails;
        }
        throw new UsernameNotFoundException("User not found");
    }
}
