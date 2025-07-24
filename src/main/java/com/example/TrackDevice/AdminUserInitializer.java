package com.example.TrackDevice;
import com.example.TrackDevice.DTO.RegisterDTO;
import com.example.TrackDevice.repo.CSARepository;
import com.example.TrackDevice.repo.RoleRepository;
import com.example.TrackDevice.repo.UserRepository;
import com.example.TrackDevice.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Data
@Component
public class AdminUserInitializer implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CSARepository csaRepository;
    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {
        boolean userExists = userRepository.existsByEmail("admin@list.ru");
        if(!userExists){
            RegisterDTO registerDTO=new RegisterDTO();
            registerDTO.setName("Pavel");
            registerDTO.setSurname("Koval");
            registerDTO.setEmail("admin@list.ru");
            registerDTO.setPassword("root");
            registerDTO.setRole(roleRepository.getById(2));
            registerDTO.setCsa(csaRepository.getById(2));
            userService.regNewUser(registerDTO);
            System.out.println("user 'admin@list.ru' created");
        } else {
            System.out.println("user 'admin@list.ru' exists");
        }


    }
}
