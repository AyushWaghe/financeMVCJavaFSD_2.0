package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.APIResponse;
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

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable int id){
        UserDetail userDetail=userService.getUserDetails(id);
        UserDetailsResponse userDetailsResponse=new UserDetailsResponse(
                userDetail.getNeeds(),
                userDetail.getWants(),
                userDetail.getSavings(),
                userDetail.getTotalBal(),
                userDetail.getAddress(),
                userDetail.getUsername()
                );
        return new ResponseEntity<>(userDetailsResponse,HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<APIResponse<UserDetailsResponse>> saveUserDetails(@Valid @RequestBody UserDetailRequest userDetailRequest){
        UserDetail userDetail=userService.saveUserDetails(userDetailRequest);

        UserDetailsResponse userDetailsResponse=new UserDetailsResponse(
                userDetail.getNeeds(),
                userDetail.getWants(),
                userDetail.getSavings(),
                userDetail.getTotalBal(),
                userDetail.getAddress(),
                userDetail.getUsername()
        );

        APIResponse apiResponse=new APIResponse();
        apiResponse.setData(userDetailsResponse);
        apiResponse.setMessage("User details saved successfully");
        apiResponse.isSuccess();

        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);

    }
}
