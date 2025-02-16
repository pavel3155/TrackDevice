package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.filter.FilterOrders;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.OrderRepository;
import com.example.TrackDevice.service.FileService;
import com.example.TrackDevice.service.OrdersService;
import com.example.TrackDevice.specification.OrdersSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Query")
public class QueryController {
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrdersSpecification ordersSpecification;
    @Autowired
    FileService fileService;

    /**
     * метод создает список заявок со сроком исполнения более 5 дней - "orders",
     * добавляет его в "model" и выводит в виде таблице на странице "overdueOrders"
     * @param model
     * @return
     */
    @GetMapping("/overdueOrders")
    public String overdueOrders(Model model) {
        System.out.println("GET:/overdueOrders...");
        List<Long> lstIdOrders = orderRepository.idOrdersDiffDaysGT5();
        System.out.println("lstIdOrders="+lstIdOrders);
        List<Order> orders= orderRepository.findByIdIn(lstIdOrders);
        System.out.println("orders="+orders);
        System.out.println("orders.size()="+orders.size());
        model.addAttribute("orders", orders);
        return "/Query/overdueOrders";
    }
    /** метод выполняется при нажатии на кнопку Просмотр на странице overdueOrders
     * метод принимает id заявки, загружает страницу viewOrder
     */
    @GetMapping("/viewOrder")
    public String viewOrder(@RequestParam long id, Model model) {
        System.out.println("GET:/viewOrder....");
        Order order = orderRepository.getById(id);
        System.out.println("order:= "+order);
        OrdersDTO ordersDTO =ordersService.newOrderDTOtoObject(order);
        List<String> fileNames;
        String directory;
        if (ordersDTO.getNum()!=null) {
            System.out.println("ordersDTO.getNum()!=null...");
            fileNames = fileService.getAllFiles(ordersDTO.getNum(),"pic");
            directory=ordersDTO.getNum();
        } else {
            fileNames=new ArrayList<>();
            directory="";
        }

        System.out.println("directory:="+directory);
        System.out.println("fileNames:= " +fileNames);
        System.out.println("ordersDTO:= " +ordersDTO);

        model.addAttribute("directory", directory);
        model.addAttribute("files", fileNames);
        model.addAttribute("ordersDTO", ordersDTO);
        return "/Query/viewOrder";
    }
}
