package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserRepository;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.AuthResult;
import org.example.exceptions.UserExistsException;
import org.example.models.User;
import org.example.models.UserDetail;
import org.example.security.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService { //Signup


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  //This bean is injected/autowired from the spring security config which we have created
    private final JwtService jwtService;


    public AuthResult registerUser(AuthRequest authRequest) {

        String useremail= authRequest.getUseremail();

        if(userRepository.existsByUseremail(useremail)){
            throw new UserExistsException("User with this email already exists");
        }

        String encodedPassword =passwordEncoder.encode(authRequest.getPassword());
        UserDetail userDetail=new UserDetail(); //My own applications user detail not of spring's sec
        org.example.models.User user=new org.example.models.User(encodedPassword, useremail, userDetail);
        userDetail.setUser(user);
        org.example.models.User savedUser=userRepository.save(user);
        String token=jwtService.generateAccessToken(new CustomUserDetails(user));
        AuthResponse authResponse=new AuthResponse(true,savedUser.getUserId(),useremail,"User registered successfully");

        return new AuthResult(authResponse,token);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //THIS METHOD IS CALLED DURING LOGIN by Authentication provider which is intern called by authentication manager
        User user=userRepository.findByUseremail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
