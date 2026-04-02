package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.UserDetailRequest;
import org.example.dto.UserDetailsResponse;
import org.example.models.UserDetail;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<UserDetailsResponse> getUserDetails(@RequestParam("userid") int id){
        Optional<UserDetail> userDetail=userService.getUserDetails(id);

        if (userDetail.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        UserDetailsResponse userDetailsResponse=new UserDetailsResponse(
                userDetail.get().getNeeds(),
                userDetail.get().getWants(),
                userDetail.get().getSavings(),
                userDetail.get().getTotalBal(),
                userDetail.get().getAddress(),
                userDetail.get().getUsername()
                );

        return new ResponseEntity<>(userDetailsResponse,HttpStatus.OK);

    }
}
