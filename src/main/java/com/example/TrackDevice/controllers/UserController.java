package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.ActDevDTO;
import com.example.TrackDevice.DTO.RegisterDTO;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.CSARepository;
import com.example.TrackDevice.repo.RoleRepository;
import com.example.TrackDevice.repo.UserRepository;
import com.example.TrackDevice.service.UserService;
import com.example.TrackDevice.show.ShowUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CSARepository csaRepository;
    @GetMapping("/showUsers")
    public String showUsers(Model model) {
        List<User> lstUsers=userRepository.findAll();
        List<ShowUser> showUsers=userService.getListShowUsers(lstUsers);
        model.addAttribute("users", showUsers);
        return "/Users/showUsers";
    }

    /** метод выполняется при нажатии на кнопку "Редактировать" на странице "showUsers",
     * метод принимает id акта, загружает данные пользователя и отображает их на странице "regUser"
     * для редактирования
     */
    @GetMapping("/editUser")
    public String getEditUser(@RequestParam long id, Model model) {
        System.out.println("GET:/editUser{id}....");
        User user=userRepository.getById(id);
        System.out.println("user:= "+user);

        RegisterDTO registerDTO=userService.convertUserToRegisterDTO(user);
        System.out.println("registerDTO:= "+registerDTO);

        List<Roles> roles =roleRepository.findAll();
        List<CSA> csas = csaRepository.findAll();


        model.addAttribute("roles", roles);
        model.addAttribute("csas", csas);
        model.addAttribute("registerDTO", registerDTO);

        return "Users/regUser";
    }
    /**
     * метод выполняет сохранение изменений свойств User в БД
     * при нажатии на кнопку "Сохранить" на странице "regUser"
     */
    @PostMapping("/editUser")
    public String saveEditUser(Model model, @Valid @ModelAttribute RegisterDTO registerDTO, BindingResult result){
        System.out.println("POST:/editUser....");
        System.out.println("registerDTO:="+registerDTO);
        List<Roles> roles =roleRepository.findAll();
        List<CSA> csas = csaRepository.findAll();
        model.addAttribute("roles",roles);
        model.addAttribute("csas",csas);
        //если пароль не совпадает, то  добавляеся ошибка в result
        if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())){
            result.addError(new FieldError("registerDTO",
                    "confirmPassword",
                    "Пароль не сходится!"));
            return "Users/regUser";
        }

        //если ошибки есть
        if(result.hasErrors()){
            return "Users/regUser";
        }
        try {
            userService.updUser(registerDTO);
            model.addAttribute("registerDTO", new RegisterDTO());
            model.addAttribute("success",true);
        }
        catch (Exception ex){
            result.addError(new FieldError("registerDTO","name",ex.getMessage()));
        }

        return "Users/regUser";
    }

    /** метод выполняется при нажатии на кнопку "Добавить пользоаптеля" на странице "showUsers",
     * метод загружает страницу "regUser" для заполнения свойств пользователя
     * для редактирования
     */
    @GetMapping("/regUser")
    public String regUser(Model model) {
        System.out.println("GET:/regUser....");
        RegisterDTO registerDTO = new RegisterDTO();
        Roles role=roleRepository.findByType("---");
        CSA csa=csaRepository.getByNum("---");
        registerDTO.setCsa(csa);
        registerDTO.setRole(role);
        model.addAttribute(registerDTO);
        List<Roles> roles =roleRepository.findAll();
        List<CSA> csas = csaRepository.findAll();
        model.addAttribute("roles",roles);
        model.addAttribute("csas",csas);
        model.addAttribute("registerDTO", registerDTO);
        return "Users/regUser";
    }

    @PostMapping("/regUser")
    public String regUser(Model model, @Valid @ModelAttribute RegisterDTO registerDTO, BindingResult result){
        System.out.println("POST:/regUser....");
        System.out.println("registerDTO:="+registerDTO);

        List<Roles> roles =roleRepository.findAll();
        List<CSA> csas = csaRepository.findAll();
        model.addAttribute("roles",roles);
        model.addAttribute("csas",csas);

        //если пароль не совпадает, то  добавляеся ошибка в result
        if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())){
            result.addError(new FieldError("registerDTO",
                                                "confirmPassword",
                                        "Пароль не сходится!"));
            return "Users/regUser";
        }
        //если id = 0, то выполняется проверка на существование email
        if(registerDTO.getId()==0) {
            // если пользователь с введенным email уже существует, то добавляеся ошибка в result
            User user = userRepository.findByEmail(registerDTO.getEmail());
            if (user != null) {
                result.addError((new FieldError("registerDTO",
                        "email",
                        "Данный Email уже занят!")));
                return "Users/regUser";
            }
        }
        //если ошибки есть
        if(result.hasErrors()){
            return "Users/regUser";
        }
        try {
            if(registerDTO.getId()==0) {
                userService.regNewUser(registerDTO);
            } else{
                userService.updUser(registerDTO);
            }
            model.addAttribute("registerDTO", registerDTO);
            model.addAttribute("success",true);
        }
        catch (Exception ex){
            result.addError(new FieldError("registerDTO","name",ex.getMessage()));
        }
        return "Users/regUser";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        System.out.println("GET:access-denied...");
        return "/access-denied";
    }
}
