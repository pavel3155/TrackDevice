package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.*;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.repo.CSARepository;
import com.example.TrackDevice.repo.DeviceRepository;
import com.example.TrackDevice.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    CSARepository csaRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    OrdersService ordersService;
    @GetMapping("/Orders")
    public String Orders(Model model) {
        List<CSA> csas = csaRepository.findAll();
        List<Device> devices =deviceRepository.findAll();
        OrdersDTO ordersDTO=new OrdersDTO();
        model.addAttribute("csas", csas);
        model.addAttribute("devices", devices);
        model.addAttribute(ordersDTO);
        return "Orders";
    }

    @PostMapping("/Orders")
    public String Orders(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO, BindingResult result) {
        //если пароль не совпадает, то  добавляеся ошибка в result
//        if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())){
//            result.addError(new FieldError("registerDTO",
//                    "confirmPassword",
//                    "Пароль не сходится!"));
//        }
//
//        // если пользователь с введенным email уже существует, то добавляеся ошибка в result
//        User user = userRepository.findByEmail(registerDTO.getEmail());
//        if(user != null){
//            result.addError((new FieldError("registerDTO",
//                    "email",
//                    "Данный Email уже занят!")));
//        }
        //если ошибки есть
        if (result.hasErrors()) {
            List<CSA> csas = csaRepository.findAll();
            List<Device> devices = deviceRepository.findAll();
            model.addAttribute("csas", csas);
            model.addAttribute("devices", devices);
            return "regUser";
        }
        try {
            ordersService.addOrders(ordersDTO);
            List<CSA> csas = csaRepository.findAll();
            List<Device> devices = deviceRepository.findAll();
            model.addAttribute("csas", csas);
            model.addAttribute("devices", devices);
            model.addAttribute("ordersDTO", new OrdersDTO());
            model.addAttribute("success", true);

        } catch (Exception ex) {
            result.addError(new FieldError("ordersDTO", "name", ex.getMessage()));
        }

        return "Orders";
    }
    @GetMapping("/addOrder")
    public String addOrder(Model model,@Valid @ModelAttribute Device device,@Valid @ModelAttribute CSA csa, BindingResult result) {
        System.out.println("/addOrder....");
        System.out.println("csa:=" +csa);
        OrdersDTO ordersDTO=new OrdersDTO();
        if (device!=null){
            ordersDTO.setDevice(device);
        }
        if (csa!=null){
            ordersDTO.setCsa(csa);
        }
        model.addAttribute("ordersDTO", ordersDTO);
        return "addOrder";
    }

    @GetMapping("/addOrder/selCSA{id}")
    public String selCSA(@PathVariable(value ="id") long id, Model model){
        System.out.println("idCSA= "+id);
        CSA csa=csaRepository.getById(id);
        System.out.println("/addOrder/selCSA{id}...");
        System.out.println("csa:=" +csa);
        model.addAttribute("csa",csa);
        return "addOrder";
    }


}

