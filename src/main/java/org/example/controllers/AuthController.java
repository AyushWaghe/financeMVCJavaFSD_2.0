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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    @Value("${cookie.same-site}")
    private String sameSite;

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

        System.out.println(authRequest.getUseremail());
        System.out.println(authRequest.getPassword());
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
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest,HttpServletResponse response){
        AuthResult authResult=authService.loginUser(authRequest);
        AuthResponse authResponse=authResult.authResponse();
        if(authResponse.isSuccess()){
            String jwt=authResult.jwt();
            ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .secure(cookieSecure)
                    .sameSite(sameSite)
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new ResponseEntity<>(authResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(authResponse,HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(
                new AuthResponse(true, null, null, "Logged out successfully")
        );
    }
}
