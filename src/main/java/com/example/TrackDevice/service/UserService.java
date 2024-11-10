package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.RegisterDTO;
import com.example.TrackDevice.model.Roles;
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
        System.out.println("Пользователь: "+user);
        System.out.println("Пользователь email : "+user.getEmail());
        System.out.println("ОРоль: "+user.getRole());
        String typeRole = user.getRole().getType();
        System.out.println("Роль: "+typeRole);
        //System.out.println("Роль: "+user.getRole().getType());
        if (user != null){
            var spUser = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(typeRole)
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
        newUser.setRole(registerDTO.getRole());
        newUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
        return userRepository.save(newUser);
    }


}
