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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
        types.remove(0);
        model.addAttribute("types",types);
        DeviceDTO deviceDTO = new DeviceDTO();
        model.addAttribute(deviceDTO);
        return "device";
    }

    @GetMapping("/device{idModel}&{filterSN}")
    @ResponseBody
    public ResponseEntity<String> loadDevice(@PathVariable String idModel, @PathVariable String filterSN){
        System.out.println("/device{idModel}_id= "+idModel);
        System.out.println("/device{idModel}_filterSN= "+filterSN);
        ModelDevice model =modelDeviceRepository.getById(Long.parseLong(idModel));
        System.out.println("model:= "+model);
        List<Device> devices = new ArrayList<>();

        if (filterSN.isEmpty()){
            devices = deviceRepository.findByModel(model);
        } else {
            devices = deviceRepository.findByModelAndSernumContainingIgnoreCase(model,filterSN);
        }

        List<JSONDeviceDTO> jsonDeviceDTOList= new ArrayList<>();
        for (Device dev:devices){
            JSONDeviceDTO jsonDeviceDTO=new JSONDeviceDTO();
            jsonDeviceDTO.setId(dev.getId());
            jsonDeviceDTO.setType(dev.getModel().getType().getType());
            jsonDeviceDTO.setModel(dev.getModel().getName());
            jsonDeviceDTO.setInvnum(dev.getInvnum());
            jsonDeviceDTO.setSernum(dev.getSernum());
            jsonDeviceDTOList.add(jsonDeviceDTO);
        }
        System.out.println("devices:= "+devices);
        System.out.println("jsonDeviceDTOList:= "+jsonDeviceDTOList);

        Gson gson = new Gson();
        String jsonDevices = gson.toJson(jsonDeviceDTOList);
        return  ResponseEntity.ok(jsonDevices);
    }

    @GetMapping("/selDevice")
    public String selDevice(@ModelAttribute OrdersDTO ordersDTO, Model model) {
        System.out.println("Get:/selDevice...");
        System.out.println("ordersDTO:= "+ordersDTO);

        List<TypeDevice> types = typeDeviceRepository.findAll();
        types.remove(0);
        model.addAttribute("types",types);
//        DeviceDTO deviceDTO = new DeviceDTO();
//        model.addAttribute(deviceDTO);
        model.addAttribute("ordersDTO",ordersDTO);
        return "selDevice";
    }

    @PostMapping("/selDevice")
    public String selDevice(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO,
                         BindingResult result, RedirectAttributes atrRedirect){
        System.out.println("POST:/selDevice...");
        System.out.println("ordersDTO:= "+ordersDTO);
        model.addAttribute("ordersDTO",ordersDTO);
        atrRedirect.addFlashAttribute("ordersDTO",ordersDTO);
        return "redirect:/selDevice";
    }

//    @GetMapping("/device{idModel}")
//    @ResponseBody
//    @CacheEvict
//    public ResponseEntity<String> loadDevice(@PathVariable String idModel){
//        System.out.println("/device{idModel}_id= "+idModel);
//        ModelDevice model =modelDeviceRepository.getById(Long.parseLong(idModel));
//        System.out.println("model:= "+model);
//        List<Dev> devices = deviceRepository.findByModel(model);
//        List<JSONDeviceDTO> jsonDeviceDTOList= new ArrayList<>();
//        for (Dev dev:devices){
//            JSONDeviceDTO jsonDeviceDTO=new JSONDeviceDTO();
//            jsonDeviceDTO.setId(dev.getId());
//            jsonDeviceDTO.setType(dev.getModel().getType().getType());
//            jsonDeviceDTO.setModel(dev.getModel().getName());
//            jsonDeviceDTO.setInvnum(dev.getInvnum());
//            jsonDeviceDTO.setSernum(dev.getSernum());
//            jsonDeviceDTOList.add(jsonDeviceDTO);
//        }
//        System.out.println("devices:= "+devices);
//        System.out.println("jsonDeviceDTOList:= "+jsonDeviceDTOList);
//
//        Gson gson = new Gson();
//        String jsonDevices = gson.toJson(jsonDeviceDTOList);
//        return  ResponseEntity.ok(jsonDevices);
//    }


//    @GetMapping("/addDevice{id}")
//    public String addDevice(@PathVariable(value ="id") long id, Model model){
//        System.out.println("id= "+id);
//        ModelDevice modelDevice = modelDeviceRepository.getById(id);
//        //DeviceDTO deviceDTO =new DeviceDTO();
//        POSTDeviceDTO postDeviceDTO = new POSTDeviceDTO();
//        postDeviceDTO.setIdModel(modelDevice.getId());
//        postDeviceDTO.setType(modelDevice.getType().getType());
//        postDeviceDTO.setModelDevice(modelDevice.getName());
//        System.out.println("postDeviceDTO"+postDeviceDTO);
//
////        Device device=new Device();
////        model.addAttribute("device",device);
////        model.addAttribute("modelDevice",modelDevice);
//        model.addAttribute("postDeviceDTO",postDeviceDTO);
//        return "addDevice";
//    }


    @GetMapping("/addDevice{id}")
    public String addDevice(@PathVariable(value ="id") long id, Model model){
        System.out.println("id= "+id);
        ModelDevice modelDevice = modelDeviceRepository.getById(id);
        System.out.println("modelDevice"+modelDevice);
        DeviceDTO deviceDTO =new DeviceDTO();
        deviceDTO.setModelDevice(modelDevice);
        System.out.println("deviceDTO"+deviceDTO);
        model.addAttribute("deviceDTO",deviceDTO);
        return "addDevice";
    }

    @PostMapping("/addDevice")
    public String addDevice(Model model, @Valid @ModelAttribute DeviceDTO deviceDTO, BindingResult result){
        System.out.println("deviceDTO:= "+deviceDTO);

        if(result.hasErrors()){
            model.addAttribute("deviceDTO", deviceDTO);
            return "addDevice";
        }
        try {
            deviceService.addDevice(deviceDTO);
            model.addAttribute("deviceDTO", deviceDTO);
            model.addAttribute("success",true);
        }
        catch (Exception ex){
            result.addError(new FieldError("deviceDTO","name",ex.getMessage()));
        }
        return "addDevice";
    }
    @GetMapping("/editDevice{id}")
    public String editDevice(@PathVariable(value ="id") long id, Model model){
        System.out.println("idDev= "+id);
        Device device=deviceRepository.getById(id);

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getId());
        deviceDTO.setModelDevice(device.getModel());
        deviceDTO.setInvnum(device.getInvnum());
        deviceDTO.setSernum(device.getSernum());
        System.out.println("deviceDTO:=" +deviceDTO);
        model.addAttribute("deviceDTO",deviceDTO);
        return "editDevice";
    }


    @PostMapping("/editDevice")
    public String editDevice(Model model, @Valid @ModelAttribute DeviceDTO deviceDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            model.addAttribute("deviceDTO", deviceDTO);
            return "editDevice";
        }
        try {
            System.out.println("deviceDTO:= "+deviceDTO);
            deviceService.saveDevice(deviceDTO);
            model.addAttribute("deviceDTO", deviceDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("deviceDTO", "name", ex.getMessage()));
        }
        return "editDevice";
    }

//    @PostMapping("/addDevice")
//    public String addDevice(Model model, @Valid @ModelAttribute POSTDeviceDTO postDeviceDTO, BindingResult result){
//        System.out.println("postDeviceDTO:= "+postDeviceDTO);
//
//
//    if(result.hasErrors()){
//            model.addAttribute("postDeviceDTO", postDeviceDTO);
//            return "addDevice";
//        }
//        try {
//            ModelDevice modelDevice =modelDeviceRepository.getById(postDeviceDTO.getIdModel());
//            System.out.println("modelDevice"+modelDevice);
//            Device device = new Device();
//            device.setModel(modelDevice);
//            device.setInv_num(postDeviceDTO.getInv_num());
//            device.setSer_num(postDeviceDTO.getSer_num());
//            deviceService.addDevice(device);
//            //ModelDevice modelDevice=deviceDTO.getModelDevice();
//            model.addAttribute("postDeviceDTO", postDeviceDTO);
////            model.addAttribute("modelDevice",modelDevice);
//            model.addAttribute("success",true);
//        }
//        catch (Exception ex){
//            result.addError(new FieldError("postDeviceDTO","name",ex.getMessage()));
//        }
//        return "addDevice";
//    }

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
        System.out.println("/device/model{type}_type= "+type);
        TypeDevice typeDevice = typeDeviceRepository.findByType(type);
        List<ModelDevice> models =modelDeviceRepository.findByType(typeDevice);
        Gson gson = new Gson();
        String jsonModels = gson.toJson(models);
        return  ResponseEntity.ok(jsonModels);
    }

//    @PostMapping("/model")
//    public String addModelDevice(Model model, @Valid @ModelAttribute TypeDeviceDTO typeDeviceDTO, BindingResult result){
//        System.out.println("typeDeviceDTO:= "+typeDeviceDTO);
//
//        if(result.hasErrors()){
//            return "model";
//        }
//        try {
//            model.addAttribute("typeDeviceDTO", typeDeviceDTO);
//            ModelDeviceDTO modelDeviceDTO = new ModelDeviceDTO();
//            model.addAttribute("modelDeviceDTO",modelDeviceDTO);
//        }
//        catch (Exception ex){
//            result.addError(new FieldError("typeDeviceDTO","name",ex.getMessage()));
//        }
//        return "add-model-dev";
//    }
    @GetMapping("/type")
    public String viewTypeDevice(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        types.remove(0);
        model.addAttribute("types", types);
        return "type";
    }

    @GetMapping("/editTypeDev{id}")
    public String editTypeDevice(@PathVariable(value ="id") long id, Model model){
        System.out.println("id= "+id);
        TypeDevice typeDevice=typeDeviceRepository.getById(id);
        TypeDeviceDTO typeDeviceDTO =new TypeDeviceDTO();
        typeDeviceDTO.setId(typeDevice.getId());
        typeDeviceDTO.setType(typeDevice.getType());
        System.out.println("type:=" +typeDevice);
        model.addAttribute("typeDeviceDTO",typeDeviceDTO);
        return "editTypeDev";
    }

    @PostMapping("/editTypeDev")
    public String editTypeDev(Model model, @Valid @ModelAttribute TypeDeviceDTO typeDeviceDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            return "editTypeDev";
        }
        try {
            System.out.println("typeDeviceDTO:= "+typeDeviceDTO);
            deviceService.saveTypeDevice(typeDeviceDTO);
            model.addAttribute("typeDeviceDTO",typeDeviceDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("typeDeviceDTO", "name", ex.getMessage()));
        }
        return "editTypeDev";
    }

    @GetMapping("/addTypeDev")
    public String addTypeDevice(Model model){
        TypeDeviceDTO typeDeviceDTO =new TypeDeviceDTO();
        model.addAttribute("typeDeviceDTO",typeDeviceDTO);
        return "addTypeDev";
    }
    @PostMapping("/addTypeDev")
    public String addTypeDev(Model model, @Valid @ModelAttribute TypeDeviceDTO typeDeviceDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            return "addTypeDev";
        }
        try {
            System.out.println("typeDeviceDTO:= "+typeDeviceDTO);
            deviceService.addTypeDevice(typeDeviceDTO);
            model.addAttribute("typeDeviceDTO",typeDeviceDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("typeDeviceDTO", "name", ex.getMessage()));
        }
        return "addTypeDev";
    }


    @GetMapping("/editModelDev{id}")
    public String editModelDevice(@PathVariable String id, Model model){
        System.out.println("id= "+id);
        ModelDevice modelDevice=modelDeviceRepository.getById(Long.parseLong(id));
        ModelDeviceDTO modelDeviceDTO = new ModelDeviceDTO();
        modelDeviceDTO.setId(modelDevice.getId());
        modelDeviceDTO.setType(modelDevice.getType());
        modelDeviceDTO.setName(modelDevice.getName());
        System.out.println("type:=" +modelDeviceDTO.getType());
        model.addAttribute("modelDeviceDTO",modelDeviceDTO);
        return "editModelDev";
    }


    @PostMapping("/editModelDev")
    public String editModelDev(Model model, @Valid @ModelAttribute ModelDeviceDTO modelDeviceDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            return "editModelDev";
        }
        try {
            System.out.println("modelDeviceDTO:= "+modelDeviceDTO);
            deviceService.saveModelDevice(modelDeviceDTO);
            model.addAttribute("modelDeviceDTO",modelDeviceDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("modelDeviceDTO", "name", ex.getMessage()));
        }
        return "editModelDev";
    }

    @GetMapping("/addModelDev{id}")
    public String addModelDevice(@PathVariable String id, Model model){
        System.out.println("addModelDev{id}_id= "+id);
        TypeDevice typeDevice = typeDeviceRepository.getById(Long.parseLong(id));
        System.out.println("addModelDev{id}_type:=" +typeDevice);
        ModelDeviceDTO modelDeviceDTO = new ModelDeviceDTO();
        modelDeviceDTO.setType(typeDevice);
        //model.addAttribute("typeDevice",typeDevice);

        model.addAttribute("modelDeviceDTO",modelDeviceDTO);
        return "addModelDev";
    }

    @PostMapping("/addModelDev")
    public String addModelDev(Model model, @Valid @ModelAttribute ModelDeviceDTO modelDeviceDTO, BindingResult result) {
        System.out.println("modelDeviceDTO:= "+modelDeviceDTO);

        //если ошибки есть
        if (result.hasErrors()) {
            return "addModelDev";
        }
        try {
            System.out.println("modelDeviceDTO:= " + modelDeviceDTO);
            deviceService.addModelDevice(modelDeviceDTO);
            TypeDevice typeDevice=modelDeviceDTO.getType();
            //model.addAttribute("typeDevice",typeDevice);
            model.addAttribute("modelDeviceDTO", modelDeviceDTO);
            model.addAttribute("success", true);
        } catch (Exception ex) {
            result.addError(new FieldError("modelDeviceDTO", "name", ex.getMessage()));
        }
        return "addModelDev";
    }
}
