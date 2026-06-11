package org.example.services;


import org.example.dao.UserDetailsRepository;
import org.example.dao.UserRepository;
import org.example.dto.UserDetailRequest;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.models.User;
import org.example.models.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UserRepository userRepository;

    public UserDetail getUserDetails(Integer id){
        Optional<UserDetail> userDetail=userDetailsRepository.findById(id);

        if(userDetail.isEmpty()){
            throw new UserDetailNotFoundException("User details not found");
        }

        return userDetail.get();
    }

    public UserDetail saveUserDetails(UserDetailRequest userDetailRequest) {
        User user=userRepository.getReferenceById(userDetailRequest.getId());
        UserDetail userDetail=userDetailsRepository.findById(userDetailRequest.getId()).orElseThrow(()-> new UserDetailNotFoundException("User details not found"));
        userDetail.setUser(user);
        userDetail.setAddress(userDetailRequest.getAddress());
        userDetail.setUsername(userDetailRequest.getName());
        userDetail.setSavings(userDetailRequest.getSavings());
        userDetail.setTotalBal(userDetailRequest.getTotal_bal());
        userDetail.setWants(userDetailRequest.getWants());
        userDetail.setNeeds(userDetailRequest.getNeeds());

        return userDetailsRepository.save(userDetail);
    }
}
