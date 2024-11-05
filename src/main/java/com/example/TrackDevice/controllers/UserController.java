package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.RegisterDTO;
import com.example.TrackDevice.model.User;
import com.example.TrackDevice.repo.UserRepository;
import com.example.TrackDevice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/regUser")
    public String regUser(Model model) {
        RegisterDTO registerDTO = new RegisterDTO();
        model.addAttribute(registerDTO);
        return "regUser.html";
    }

    @PostMapping("/regUser")
    public String regUser(Model model, @Valid @ModelAttribute RegisterDTO registerDTO, BindingResult result){
        //если пароль не совпадает, то  добавляеся ошибка в result
        if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())){
            result.addError(new FieldError("registerDTO",
                                                "confirmPassword",
                                        "Пароль не сходится!"));
        }

        // если пользователь с введенным email уже существует, то добавляеся ошибка в result
        User user = userRepository.findByEmail(registerDTO.getEmail());
        if(user != null){
            result.addError((new FieldError("registerDTO",
                                                  "email",
                                          "Данный Email уже занят!")));
        }
        //если ошибки есть
        if(result.hasErrors()){
            return "regUser";
        }
        try {
            userService.regNewUser(registerDTO);
            model.addAttribute("registerDTO", new RegisterDTO());
            model.addAttribute("success",true);
        }
        catch (Exception ex){
            result.addError(new FieldError("registerDTO","name",ex.getMessage()));
        }

        return "regUser";
    }
}
