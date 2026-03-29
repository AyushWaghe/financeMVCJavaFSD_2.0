package org.example.services;

import org.example.dao.UserRepository;
import org.example.dto.AuthResponse;
import org.example.models.User;
import org.example.models.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    public AuthResponse registerUser(String useremail,String password){
//        User user=new User(password,useremail);


        try {
            UserDetail userDetail=new UserDetail();
            User user=new User(password,useremail,userDetail);
            userDetail.setUser(user);
            User savedUser=userRepository.save(user);

            return new AuthResponse(true,savedUser.getUserId(),useremail,"User saved successfully");
        }catch (Exception e){
            System.out.println(e);
            return new AuthResponse(false,null,null,e.toString());
        }

    }
}
