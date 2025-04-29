package com.example.TrackDevice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        System.out.println("GET:access-denied...");
        return "/access-denied";
    }
}
