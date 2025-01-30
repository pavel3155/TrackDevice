package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.ActDevDTO;
import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.*;
import com.example.TrackDevice.service.ActDevService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
    DeviceRepository deviceRepository;
    @Autowired
    ActTypesRepository actTypesRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    TypeDeviceRepository typeDeviceRepository;


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
    @GetMapping("/ActDev")
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
        actDevDTO.setIdSelDev(actDev.getDevice().getId());
        actDevDTO.setFromCSA(actDev.getFromCSA());
        actDevDTO.setIdFromCSA(actDev.getFromCSA().getId());
        actDevDTO.setToCSA(actDev.getToCSA());
        actDevDTO.setOrder(actDev.getOrder());
        actDevDTO.setNote(actDev.getNote());


        System.out.println("actDevDTO:= "+actDevDTO);
        List<CSA> csas = csaRepository.findAll();
        List<ActTypes> types =actTypesRepository.findAll();

        model.addAttribute("types", types);
        model.addAttribute("csas", csas);
        model.addAttribute("actDevDTO", actDevDTO);

        return "Acts/ActDev";
    }

    @PostMapping("/ActDev")
    public String createActDev(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO){
        System.out.println("POST:/ActDev...");
        System.out.println("createActDev...");
        System.out.println("ordersDTO:= "+ordersDTO);

        ActDevDTO actDevDTO = new ActDevDTO();
        actDevDTO.setActType(ordersDTO.getActTypes());

        LocalDate date =LocalDate.now();
        actDevDTO.setDate(date.toString());

        String num = ordersDTO.getActTypes().getType().substring(13,15)+"-"+
                ordersDTO.getCsa().getNum().substring(0,2)+"/__-"+
                ordersDTO.getActTypes().getType().substring(1,2)+"-"+
                String.valueOf(date.getYear()).substring(2);
        System.out.println("num:="+num);
        actDevDTO.setNum(num);

        if (ordersDTO.getActTypes().getType().substring(1,2).equals("Р")) {
            actDevDTO.setFromCSA(csaRepository.getByNum("02C001"));
            actDevDTO.setToCSA(ordersDTO.getCsa());
            actDevDTO.setDevice(deviceRepository.findBySernum("---").get(0));

        } else if(ordersDTO.getActTypes().getType().substring(1,2).equals("П")){
            actDevDTO.setFromCSA(ordersDTO.getCsa());
            actDevDTO.setToCSA(csaRepository.getByNum("02C001"));
            actDevDTO.setDevice(ordersDTO.getDevice());
        }
        actDevDTO.setOrder(orderRepository.getById(ordersDTO.getId()));
        actDevDTO.setNote("");

        System.out.println("actDevDTO:= "+actDevDTO);
        List<CSA> csas = csaRepository.findAll();
        List<ActTypes> types =actTypesRepository.findAll();

        model.addAttribute("types", types);
        model.addAttribute("csas", csas);
        model.addAttribute("actDevDTO", actDevDTO);

        return "Acts/ActDev";
    }


    @PostMapping("/selDevSC")
    public String devSelect(Model model, @Valid @ModelAttribute ActDevDTO actDevDTO){
        System.out.println("POST:/DevSC...");
        System.out.println("actDevDTO:= "+actDevDTO);
        List<TypeDevice> types = typeDeviceRepository.findAll();
        types.remove(0);
        model.addAttribute("types",types);
        model.addAttribute("actDevDTO",actDevDTO);
        return "Acts/DevSC";
    }
    @PostMapping("/trDevSC")
    public String devTransfer(Model model, @Valid @ModelAttribute ActDevDTO actDevDTO){
        System.out.println("POST:/trDevSC...");
        System.out.println("actDevDTO:= "+actDevDTO);


        List<CSA> csas = csaRepository.findAll();
        List<ActTypes> types =actTypesRepository.findAll();

        model.addAttribute("types", types);
        model.addAttribute("csas", csas);
        model.addAttribute("actDevDTO", actDevDTO);

        return "Acts/ActDev";
    }


//        model.addAttribute("ordersDTO",ordersDTO);
//        atrRedirect.addFlashAttribute("ordersDTO",ordersDTO);



}

