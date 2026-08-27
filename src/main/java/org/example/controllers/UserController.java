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
@RequestMapping("/user-profile")
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
                userDetail.getAddress(),
                userDetail.getUsername(),
                userDetail.isNotificationSubscribed()
                );
        return new ResponseEntity<>(userDetailsResponse,HttpStatus.OK);
    }

    @GetMapping("/getCredits")
    @ResponseBody
    public ResponseEntity<APIResponse<Integer>> getReasoningCredits(@RequestParam("userId") Integer userId){
       Integer reasoningCredits= userService.getReasoningCredits(userId);
       APIResponse apiResponse=new APIResponse();
       apiResponse.setData(reasoningCredits);
       apiResponse.setMessage("Credits fetched successfully");
       apiResponse.setSuccess(true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/decrementCredit")
    @ResponseBody
    public ResponseEntity<APIResponse<Integer>> decrementCredits(@RequestParam("userId") Integer userId){
        Integer reasoningCredits=userService.decrementCredits(userId);
        APIResponse apiResponse=new APIResponse();
        apiResponse.setData(reasoningCredits);
        apiResponse.setMessage("Credits decremented successfully");
        apiResponse.setSuccess(true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<APIResponse<UserDetailsResponse>> saveUserDetails(@Valid @RequestBody UserDetailRequest userDetailRequest,@PathVariable("userId") Integer userId){
        System.out.println(userDetailRequest);
        UserDetail userDetail=userService.saveUserDetails(userDetailRequest,userId);

        UserDetailsResponse userDetailsResponse=new UserDetailsResponse(
                userDetail.getNeeds(),
                userDetail.getWants(),
                userDetail.getSavings(),
                userDetail.getAddress(),
                userDetail.getUsername(),
                userDetail.isNotificationSubscribed()
        );

        APIResponse<UserDetailsResponse> apiResponse=new APIResponse<>();
        apiResponse.setData(userDetailsResponse);
        apiResponse.setMessage("User details saved successfully");
        apiResponse.setSuccess(true);

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);

    }
}
