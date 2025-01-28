package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.ActDevDTO;
import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.ActDevRepository;
import com.example.TrackDevice.repo.ActTypesRepository;
import com.example.TrackDevice.repo.CSARepository;
import com.example.TrackDevice.service.ActDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Acts")
public class ActDevController {
    @Autowired
    ActDevRepository actDevRepository;
    @Autowired
    ActDevService actDevService;
    @Autowired
    CSARepository csaRepository;
    @Autowired
    ActTypesRepository actTypesRepository;

    @GetMapping("/ActsDev")
    public String ActsDev(Model model) {
        System.out.println("GET:/ActsDev...");
        List<ActDev> devActs = actDevRepository.findAll();
        System.out.println("devActs ="+devActs);
        model.addAttribute("devActs", devActs);

    return "/Acts/ActsDev";
    }
    /** метод выполняется при нажатии на кнопку "Открыть" на странице "ActsDev"
     * метод принимает id акта, загружает страницу "ActDev"
     */
    @GetMapping("/Acts/ActDev")
    public String getActDev(@RequestParam long id, Model model) {
        System.out.println("GET:/ActDev{id}....");
        ActDev actDev = actDevRepository.getById(id);
        System.out.println("actDev:= "+actDev);

        ActDevDTO actDevDTO = new ActDevDTO();
        actDevDTO.setId(actDev.getId());
        actDevDTO.setDate(actDev.getDate().toString());
        actDevDTO.setNum(actDev.getNum());
        actDevDTO.setActType(actDev.getTypes());
        actDevDTO.setDevice(actDev.getDevice());
        actDevDTO.setIdDevice(actDev.getDevice().getId());
        actDevDTO.setFromCSA(actDev.getFromCSA());
        actDevDTO.setToCSA(actDevDTO.getToCSA());
        actDevDTO.setOrder(actDev.getOrder());
        actDevDTO.setNote(actDev.getNote());

        List<CSA> csas = csaRepository.findAll();
        List<ActTypes> types =actTypesRepository.findAll();

        model.addAttribute("types", types);
        model.addAttribute("csas", csas);
        model.addAttribute("actDevDTO", actDevDTO);
        return "/Acts/ActDev";
    }
}

