package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.*;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.Roles;
import com.example.TrackDevice.repo.CsaRepository;
import com.example.TrackDevice.repo.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    CsaRepository csaRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    OrdersRepository ordersRepository;
    @GetMapping("/Orders")
    public String regOrders(Model model) {
        List<CSA> csas =csaRepository.findAll();
        List<Device> devices =deviceRepository.findAll();
        OrdersDTO ordersDTO=new OrdersDTO();
        model.addAttribute("csa", csas);
        model.addAttribute("devices", devices);
        model.addAttribute(ordersDTO);
        return "Orders";
    }
}

