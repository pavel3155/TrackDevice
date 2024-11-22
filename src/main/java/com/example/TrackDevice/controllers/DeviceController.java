package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.*;
import com.example.TrackDevice.model.*;
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
        TypeDeviceDTO typeDeviceDTO  = new TypeDeviceDTO();
        model.addAttribute(typeDeviceDTO);
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
    public String addModelDevice(Model model, @Valid @ModelAttribute TypeDeviceDTO typeDeviceDTO, BindingResult result){
        System.out.println("typeDeviceDTO:= "+typeDeviceDTO);

        if(result.hasErrors()){
            return "model";
        }
        try {
            model.addAttribute("typeDeviceDTO", typeDeviceDTO);
            ModelDeviceDTO modelDeviceDTO = new ModelDeviceDTO();
            model.addAttribute("modelDeviceDTO",modelDeviceDTO);
        }
        catch (Exception ex){
            result.addError(new FieldError("typeDeviceDTO","name",ex.getMessage()));
        }
        return "add-model-dev";
    }
    @GetMapping("/type")
    public String addTypeDevice(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        model.addAttribute("types", types);
        return "type";
    }

    @GetMapping("/edit-model-dev{id}")
    public String editModelDevice(@PathVariable String id, Model model){
        System.out.println("id= "+id);
        ModelDevice modelDevice=modelDeviceRepository.getById(Long.parseLong(id));
        ModelDeviceDTO modelDeviceDTO = new ModelDeviceDTO();
        modelDeviceDTO.setId(modelDevice.getId());
        modelDeviceDTO.setType(modelDevice.getType());
        modelDeviceDTO.setName(modelDevice.getName());
        System.out.println("type:=" +modelDeviceDTO.getType());
        model.addAttribute("modelDeviceDTO",modelDeviceDTO);
        return "edit-model-dev";
    }


    @PostMapping("/edit-model-dev")
    public String editModelDev(Model model, @Valid @ModelAttribute ModelDeviceDTO modelDeviceDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            return "edit-model-dev";
        }
        try {
            System.out.println("modelDeviceDTO:= "+modelDeviceDTO);
            deviceService.saveModelDevice(modelDeviceDTO);
            model.addAttribute("modelDeviceDTO",modelDeviceDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("modelDeviceDTO", "name", ex.getMessage()));
        }
        return "edit-model-dev";
    }

    @GetMapping("/add-model-dev{id}")
    public String addModelDevice(@PathVariable String id, Model model){
        System.out.println("id= "+id);
        TypeDevice typeDevice = typeDeviceRepository.getById(Long.parseLong(id));
        System.out.println("type:=" +typeDevice);
        ModelDeviceDTO modelDeviceDTO = new ModelDeviceDTO();
        model.addAttribute("typeDevice",typeDevice);
        model.addAttribute("modelDeviceDTO",modelDeviceDTO);
        return "add-model-dev";
    }

    @PostMapping("/add-model-dev")
    public String addModelDev(Model model, @Valid @ModelAttribute ModelDeviceDTO modelDeviceDTO, BindingResult result) {
        System.out.println("modelDeviceDTO:= "+modelDeviceDTO);

        //если ошибки есть
        if (result.hasErrors()) {
            return "add-model-dev";
        }
        try {
            System.out.println("modelDeviceDTO:= " + modelDeviceDTO);
            deviceService.addModelDevice(modelDeviceDTO);
            TypeDevice typeDevice=modelDeviceDTO.getType();
            model.addAttribute("typeDevice",typeDevice);
            model.addAttribute("modelDeviceDTO", modelDeviceDTO);
            model.addAttribute("success", true);
        } catch (Exception ex) {
            result.addError(new FieldError("modelDeviceDTO", "name", ex.getMessage()));
        }
        return "add-model-dev";
    }
}
