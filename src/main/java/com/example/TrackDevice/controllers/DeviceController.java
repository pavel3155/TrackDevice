package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.DTO.ModelDeviceDTO;
import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.DTO.RegisterDTO;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.Roles;
import com.example.TrackDevice.model.TypeDevice;
import com.example.TrackDevice.repo.DeviceRepository;
import com.example.TrackDevice.repo.ModelDeviceRepository;
import com.example.TrackDevice.repo.TypeDeviceRepository;
import com.example.TrackDevice.service.DeviceService;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DeviceController {

    @Autowired
    TypeDeviceRepository typeDeviceRepository;
    @Autowired
    ModelDeviceRepository modelDeviceRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    DeviceService deviceService;



    @GetMapping("/device")
    public String newDevice(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        model.addAttribute("types",types);
        DeviceDTO deviceDTO = new DeviceDTO();
        model.addAttribute(deviceDTO);
        return "device";
    }
    @PostMapping("/device")
    public String loadDevice (Model model, @Valid @ModelAttribute DeviceDTO deviceDTODTO, BindingResult result) {
        return "device";
    }

    @GetMapping("/model")
    public String ModelDevice(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        model.addAttribute("types",types);
        ModelDeviceDTO modelDeviceDTO  = new ModelDeviceDTO();
        model.addAttribute(modelDeviceDTO);
        return "model";
    }
    @GetMapping("/device/model{type}")
    @ResponseBody
    public ResponseEntity<String> loadModelDevice(@PathVariable String type){
        System.out.println("type= "+type);
        TypeDevice typeDevice = typeDeviceRepository.findByType(type);
        List<ModelDevice> models =modelDeviceRepository.findByType(typeDevice);
        Gson gson = new Gson();
        String jsonModels = gson.toJson(models);
        return  ResponseEntity.ok(jsonModels);
    }

    @PostMapping("/model")
    public String addModelDevice(Model model, @Valid @ModelAttribute ModelDeviceDTO modelDeviceDTO, BindingResult result){

        if(result.hasErrors()){
            return "model";
        }
        try {
            deviceService.addModelDevice(modelDeviceDTO);
            model.addAttribute("modelDeviceDTO", new ModelDeviceDTO());
            model.addAttribute("success",true);
        }
        catch (Exception ex){
            result.addError(new FieldError("modelDeviceDTO","name",ex.getMessage()));
        }
        return "model";
    }
    @GetMapping("/type")
    public String addTypeDevice(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        model.addAttribute("types", types);
        return "type";
    }




}
