package org.example.services;


import lombok.RequiredArgsConstructor;
import org.example.dao.UserDetailsRepository;
import org.example.dao.UserRepository;
import org.example.dto.UserDetailRequest;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.models.User;
import org.example.models.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;


    @Cacheable("user-profile")
    public UserDetail getUserDetails(Integer userId){
        Optional<UserDetail> userDetail=userDetailsRepository.findById(userId);

        if(userDetail.isEmpty()){
            throw new UserDetailNotFoundException("User details not found");
        }

        return userDetail.get();
    }

    @Transactional
    @CachePut(value = "user-profile",key = "#userId")
    public UserDetail saveUserDetails(UserDetailRequest userDetailRequest,Integer userId) {
        User user=userRepository.getReferenceById(userId);
        UserDetail userDetail=userDetailsRepository.findById(userId).orElseThrow(()-> new UserDetailNotFoundException("User details not found"));
        userDetail.setUser(user);
        userDetail.setAddress(userDetailRequest.getAddress());
        userDetail.setUsername(userDetailRequest.getName());
        userDetail.setSavings(userDetailRequest.getSavings());
        userDetail.setWants(userDetailRequest.getWants());
        userDetail.setNeeds(userDetailRequest.getNeeds());
        userDetail.setNotificationSubscribed(userDetailRequest.isNotificationSubscribed());

        return userDetailsRepository.save(userDetail);
    }
}
