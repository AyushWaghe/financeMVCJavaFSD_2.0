package org.example.services;

import org.example.dao.UserRepository;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.exceptions.UserExistsException;
import org.example.models.User;
import org.example.models.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    public AuthResponse registerUser(AuthRequest authRequest){
        try {
            String useremail= authRequest.getUseremail();

            if(userRepository.existsByUseremail(useremail)){
                throw new UserExistsException("User with this email already exists");
            }

            String password =authRequest.getPassword();
            UserDetail userDetail=new UserDetail();
            User user=new User(useremail, password, userDetail);
            userDetail.setUser(user);
            User savedUser=userRepository.save(user);

            return new AuthResponse(true,savedUser.getUserId(),useremail,"User saved successfully");
        }catch (UserExistsException e){
            System.out.println(e);
            return new AuthResponse(false,null,null,e.toString());
        }catch (Exception e){
            System.out.println(e);
            return new AuthResponse(false,null,null,e.toString());
        }

    }
}
