package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.RegisterDTO;
import com.example.TrackDevice.model.User;
import com.example.TrackDevice.repo.RoleRepository;
import com.example.TrackDevice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        System.out.println(user);
        if (user != null){
            var spUser = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().getRole())
                    .build();
            return spUser;
        }
        return null;
    }
    public User regNewUser(RegisterDTO registerDTO){

        var bCryptEncoder = new BCryptPasswordEncoder();
        User newUser = new User();
        newUser.setName(registerDTO.getName());
        newUser.setSurname(registerDTO.getSurname());
        newUser.setEmail(registerDTO.getEmail());
        System.out.println(registerDTO.getRole());
        newUser.setRole(registerDTO.getRole());
        newUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
        return userRepository.save(newUser);
    }


}
