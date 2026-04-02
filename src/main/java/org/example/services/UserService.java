package org.example.services;


import jakarta.validation.Valid;
import org.example.dao.UserDetailsRepository;
import org.example.dto.UserDetailRequest;
import org.example.models.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    public Optional<UserDetail> getUserDetails(Integer id){
        Optional<UserDetail> userDetail=userDetailsRepository.findById(id);
        return userDetail;
    }
}
