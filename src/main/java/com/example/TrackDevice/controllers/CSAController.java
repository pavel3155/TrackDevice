package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.CSADTO;
import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.DTO.TypeDeviceDTO;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.TypeDevice;
import com.example.TrackDevice.repo.CSARepository;
import com.example.TrackDevice.service.CSAService;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CSAController {
    @Autowired
    CSARepository csaRepository;
    @Autowired
    CSAService csaService;

    @GetMapping("/csa")
    public String csa(Model model) {
        List<String> codes = csaRepository.findDistinctCode();
        model.addAttribute("codes",codes);
        CSADTO csaDTO  = new CSADTO();
        model.addAttribute(csaDTO);
        return "csa";
    }
    @GetMapping("/csa{code}")
    @ResponseBody
    public ResponseEntity<String> loadCSA(@PathVariable String code){
        System.out.println("/csa{code}_code= "+code);

        List<CSA> csas =csaRepository.findByCode(code);
        System.out.println("csas= "+csas);
        Gson gson = new Gson();
        String jsonCSA = gson.toJson(csas);
        return  ResponseEntity.ok(jsonCSA);
    }
    @GetMapping("/editCSA{id}")
    public String editCSA(@PathVariable(value ="id") long id, Model model){
        System.out.println("idCSA= "+id);
        CSA csa=csaRepository.getById(id);
        CSADTO csaDTO =new CSADTO();
        csaDTO.setId(csa.getId());
        csaDTO.setCode(csa.getCode());
        csaDTO.setNum(csa.getNum());
        csaDTO.setAddress(csa.getAddress());
        System.out.println("csaDTO:=" +csaDTO);
        model.addAttribute("csaDTO",csaDTO);
        return "editCSA";
    }


    @PostMapping("/editCSA")
    public String editCSA(Model model, @Valid @ModelAttribute CSADTO csaDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            model.addAttribute("csaDTO", csaDTO);
            return "editCSA";
        }
        try {
            System.out.println("csaDTO:= "+csaDTO);
            csaService.saveCSA(csaDTO);
            model.addAttribute("csaDTO", csaDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("csaDTO", "name", ex.getMessage()));
        }
        return "editCSA";
    }

    @GetMapping("/addCSA{code}")
    public String addCSA(@PathVariable(value ="code") String code, Model model){
        System.out.println("codeCSA= "+code);
        CSADTO csaDTO =new CSADTO();
        csaDTO.setCode(code);
        System.out.println("csaDTO:=" +csaDTO);
        model.addAttribute("csaDTO",csaDTO);
        return "addCSA";
    }
    @PostMapping("/addCSA")
    public String addCSA(Model model, @Valid @ModelAttribute CSADTO csaDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            model.addAttribute("csaDTO", csaDTO);
            return "addCSA";
        }
        try {
            System.out.println("POST_csaDTO:= "+csaDTO);
            csaService.addCSA(csaDTO);
            model.addAttribute("csaDTO", csaDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("csaDTO", "name", ex.getMessage()));
        }
        return "addCSA";
    }
//    @GetMapping("/selCSA")
//    public String selCsa(@RequestParam(value ="num") String num, Model model) {
//        System.out.println("/selCSA_num:= "+num);
//        List<String> codes = csaRepository.findDistinctCode();
//        model.addAttribute("codes",codes);
//        CSA csa  = new CSA();
//        model.addAttribute(csa);
//        return "selCSA";
//    }

@GetMapping("/selCSA")
public String selCsa(@ModelAttribute OrdersDTO ordersDTO, Model model) {
    System.out.println("Get_selCSA");
    System.out.println("ordersDTO:= "+ordersDTO);
    List<String> codes = csaRepository.findDistinctCode();
    model.addAttribute("codes",codes);
    //OrdersDTO ordersDTO=new OrdersDTO();
    model.addAttribute("ordersDTO",ordersDTO);

    CSA csa  = new CSA();
    model.addAttribute(csa);
    return "selCSA";
}
    @PostMapping("/selCSA")
    public String selCsa(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO,
                         BindingResult result, RedirectAttributes atrRedirect){
        System.out.println("POST_selCSA_ordersDTO:= "+ordersDTO);
        model.addAttribute("ordersDTO",ordersDTO);
        atrRedirect.addFlashAttribute("ordersDTO",ordersDTO);
        return "redirect:/selCSA";
    }
}


