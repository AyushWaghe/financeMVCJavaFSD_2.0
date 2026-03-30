package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.exceptions.UserExistsException;
import org.example.models.User;
import org.example.services.AuthService;
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

    @GetMapping("/test")
    public String testSite(){
        return "Spring working correctly";
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRequest authRequest){
        AuthResponse authResponse=authService.registerUser(authRequest);

        if(authResponse.isSuccess()){
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(authResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
