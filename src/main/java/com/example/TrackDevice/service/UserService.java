package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.RegisterDTO;
import com.example.TrackDevice.model.User;
import com.example.TrackDevice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User regNewUser(RegisterDTO registerDTO){
        User user = new User();
        var bCryptEncoder = new BCryptPasswordEncoder();
        User newUser = new User();
        newUser.setName(registerDTO.getName());
        newUser.setSurname(registerDTO.getSurname());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setRole("user");
        newUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
        return userRepository.save(user);
    }


}
