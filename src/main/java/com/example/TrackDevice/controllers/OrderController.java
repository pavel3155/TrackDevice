package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.RegisterDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/Orders")
    public String regUser(Model model) {
//        RegisterDTO registerDTO = new RegisterDTO();
//        model.addAttribute(registerDTO);
        return "Orders";
    }
}

