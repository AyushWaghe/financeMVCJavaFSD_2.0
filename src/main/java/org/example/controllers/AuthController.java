package org.example.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.AuthResult;
import org.example.exceptions.UserExistsException;
import org.example.models.User;
import org.example.services.AuthService;
import org.example.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserDetailServiceImpl userDetailServiceImpl;

    @GetMapping("/test")
    public String testSite(){
        return "Spring working correctly";
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRequest authRequest,
                                                    HttpServletResponse response){
        AuthResult authResult=  userDetailServiceImpl.registerUser(authRequest);
        AuthResponse authResponse=authResult.authResponse();

        String jwt=authResult.jwt();

        //HTTP Cookie
        Cookie cookie=new Cookie("jwt",jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1 hour
        response.addCookie(cookie); //Browser will now store this cookie upon receiving

        if(authResponse.isSuccess()){
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(authResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest){
        AuthResponse authResponse=authService.loginUser(authRequest);

        if(authResponse.isSuccess()){
            return new ResponseEntity<>(authResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(authResponse,HttpStatus.UNAUTHORIZED);
        }
    }
}
