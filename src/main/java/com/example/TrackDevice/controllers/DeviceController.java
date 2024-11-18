package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.TypeDevice;
import com.example.TrackDevice.repo.DeviceRepository;
import com.example.TrackDevice.repo.ModelDeviceRepository;
import com.example.TrackDevice.repo.TypeDeviceRepository;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    @GetMapping("/device")
    public String newDevice(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        model.addAttribute("types",types);
//        TypeDevice type = types.getFirst();
//        System.out.println("Тип ТС: "+ type);
//        List<ModelDevice> models =modelDeviceRepository.findByType(type);
//        model.addAttribute("models",models);
//        ModelDevice modelDevice = models.getFirst();
//        System.out.println("Модель ТС: "+ modelDevice);
//        List<Device> devices =deviceRepository.findByModel(modelDevice);
//        model.addAttribute("devices",devices);
//        System.out.println("ТС: "+ devices);
        DeviceDTO deviceDTO = new DeviceDTO();
        model.addAttribute(deviceDTO);

        return "device";
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

    @GetMapping("/device/device{model}")
    public String loadDevice(@PathVariable ModelDevice modelDevice,Model model){
        List<Device> devices =deviceRepository.findByModel(modelDevice);
        model.addAttribute("devices",devices);
        return "device";
    }
    @PostMapping("/device")
    public String loadDevice (Model model, @Valid @ModelAttribute DeviceDTO deviceDTODTO, BindingResult result) {
        return "device";
    }

}
