package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.*;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.*;
import com.example.TrackDevice.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    CSARepository csaRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/Orders")
    public String Orders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("UserName:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());

        Roles roleCSA =roleRepository.findByType("CSA");
        System.out.println("roleCSA:= "+roleCSA);
        List<Order> orders;
        if(userDetails.getAuthorities().contains(roleCSA)){
            User user = userRepository.findByEmail(userDetails.getUsername());
            System.out.println("user:="+user);
            CSA csa = user.getCsa();
            System.out.println("csa:= "+csa);
            orders = orderRepository.getByCsa(csa);
        } else{
            orders = orderRepository.findAll();
        }

        List<CSA> csas = csaRepository.findAll();
        System.out.println("orders:= "+orders);
        model.addAttribute("csas", csas);
        model.addAttribute("orders",orders);
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
            ordersService.add(ordersDTO);
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
    public String Order(@ModelAttribute OrdersDTO ordersDTO,@AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("GET:/addOrder....");
        System.out.println("ordersDTO:= "+ordersDTO);

        System.out.println("Username:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());

        if (ordersDTO==null){
            System.out.println("ordersDTO==null");
            ordersDTO=new OrdersDTO();
        }
        if (ordersDTO.getCsa()==null){
            System.out.println("ordersDTO.getCsa()==null....");

            Roles roleCSA =roleRepository.findByType("CSA");
            System.out.println("roleCSA:= "+roleCSA);

            if(userDetails.getAuthorities().contains(roleCSA)){
                CSA csa= userRepository.findByEmail(userDetails.getUsername()).getCsa();
                System.out.println("csa:="+csa);
                ordersDTO.setCsa(csa);
                ordersDTO.setIdCSA(csa.getId());
            }else{
                CSA csa = csaRepository.getById(1);
                ordersDTO.setCsa(csa);
                ordersDTO.setIdCSA(1);
            }
        }
        if (ordersDTO.getDevice() ==null){
            System.out.println("ordersDTO.getDevice()==null....");
            Device device=deviceRepository.getById(1);
            ordersDTO.setDevice(device);
            ordersDTO.setIdDevice(1);
        }
        if (ordersDTO.getStatus() ==null){
            ordersDTO.setStatus("открыта");
        }
        model.addAttribute("ordersDTO", ordersDTO);
        return "addOrder";
    }

    @PostMapping("/addOrder")
    public String Order(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO,
                           BindingResult result, RedirectAttributes atrRedirect) {
        System.out.println("POST:/addOrder...");
        System.out.println("ordersDTO:= "+ordersDTO);
        ordersDTO.setCsa(csaRepository.getById(ordersDTO.getIdCSA()));
        ordersDTO.setDevice(deviceRepository.getById(ordersDTO.getIdDevice()));
        System.out.println("POST_/addOrder_ordersDTO:= "+ordersDTO);
        atrRedirect.addFlashAttribute("ordersDTO",ordersDTO);
        if(ordersDTO.getId()!=0){
            System.out.println("ordersDTO.getId():= "+ordersDTO.getId());
            return "redirect:/editOrder";
        }
    return "redirect:/addOrder";
    }


    @PostMapping("/addOrder/Add")
    public String addOrder(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO,
                           BindingResult result, RedirectAttributes atrRedirect) {
        System.out.println("POST:/addOrder/Add...");
        System.out.println("ordersDTO:= "+ordersDTO);

        if (result.hasErrors()) {
            return "addOrder";
        }
        try {
            ordersService.add(ordersDTO);
            atrRedirect.addFlashAttribute("success",true);
            atrRedirect.addFlashAttribute("ordersDTO",ordersDTO);
        } catch (Exception ex) {
            result.addError(new FieldError("ordersDTO", "name", ex.getMessage()));
        }
        return "redirect:/addOrder";
    }


    @GetMapping("/addOrder/selCSA")
    public String selCSA(@RequestParam(value ="idCSA") long csa_id, Model model){
        System.out.println("idCSA= "+csa_id);
        CSA csa=csaRepository.getById(csa_id);
        System.out.println("/addOrder/selCSA{id}...");
        System.out.println("csa:=" +csa);
        OrdersDTO ordersDTO = new OrdersDTO();
        if (csa!=null){
            ordersDTO.setCsa(csa);
        }
        model.addAttribute("ordersDTO",ordersDTO);
        return "redirect:/addOrder";
    }
    @GetMapping("/editOrder")
    public String editOrder(@ModelAttribute OrdersDTO ordersDTO, Model model) {
        System.out.println("GET:/editOrder....");
        System.out.println("ordersDTO:= "+ordersDTO);
        model.addAttribute("ordersDTO", ordersDTO);
        return "editOrder";
    }
    @GetMapping("/editOrder{id}")
    public String editOrder(@PathVariable long id, Model model) {
        System.out.println("GET:/editOrder{id}....");
        Order order = orderRepository.getById(id);
        System.out.println("order:= "+order);
        OrdersDTO ordersDTO =new OrdersDTO();
        ordersDTO.setId(order.getId());
        ordersDTO.setDate(order.getDate());
        ordersDTO.setNum(order.getNum());
        ordersDTO.setCsa(order.getCsa());
        ordersDTO.setIdCSA(order.getCsa().getId());
        ordersDTO.setDevice(order.getDevice());
        ordersDTO.setIdDevice(order.getDevice().getId());
        ordersDTO.setDescription(order.getDescription());
        ordersDTO.setStatus(order.getStatus());
        model.addAttribute("ordersDTO", ordersDTO);
        return "editOrder";
    }
    @PostMapping("/editOrder")
    public String editOrder(@Valid @ModelAttribute OrdersDTO ordersDTO, BindingResult result,Model model) {
        System.out.println("POST:/editOrder...");
        System.out.println("ordersDTO:= "+ordersDTO);
        if (result.hasErrors()) {
            return "editOrder";
        }
        try {
            ordersService.save(ordersDTO);
            model.addAttribute("success",true);
            model.addAttribute("ordersDTO",ordersDTO);
        } catch (Exception ex) {
            result.addError(new FieldError("ordersDTO", "name", ex.getMessage()));
        }
        return "/editOrder";
    }

}

