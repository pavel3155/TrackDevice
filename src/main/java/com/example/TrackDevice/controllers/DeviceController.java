package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.*;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.*;
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
    @Autowired
    DeviceCSARepository deviceCSARepository;
    @Autowired
    CSARepository csaRepository;

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
            jsonDeviceDTO.setCsa(dev.getCsa().getNum());
            jsonDeviceDTOList.add(jsonDeviceDTO);
        }
        System.out.println("devices:= "+devices);
        System.out.println("jsonDeviceDTOList:= "+jsonDeviceDTOList);
        Gson gson = new Gson();
        String jsonDevices = gson.toJson(jsonDeviceDTOList);
        return  ResponseEntity.ok(jsonDevices);
    }

    @GetMapping("/selDevice{idModel}&{filterSN}&{idCSA}")
    @ResponseBody
    public ResponseEntity<String> loadDevice(@PathVariable String idModel,
                                             @PathVariable String filterSN,
                                             @PathVariable Integer idCSA)
    {
        System.out.println("GET:/selDevice{idModel}&{filterSN}&{idCSA}...");
        System.out.println("idModel:= "+idModel);
        System.out.println("filterSN:= "+filterSN);
        System.out.println("idCSA:= "+idCSA);

        ModelDevice model =modelDeviceRepository.getById(Long.parseLong(idModel));
        System.out.println("model:= "+model);
        CSA csa = csaRepository.getById(idCSA);

        List<Device> devices = new ArrayList<>();
        if (filterSN.isEmpty()){
            if(csa==null){
                devices = deviceRepository.findByModel(model);
            } else {
                devices=deviceRepository.findByCsaAndModel(csa, model);
            }
        } else {
            if(csa==null) {
                devices = deviceRepository.findByModelAndSernumContainingIgnoreCase(model, filterSN);
            } else{
                devices= deviceRepository.findByCsaAndModelAndSernumContainingIgnoreCase(csa, model, filterSN);
            }
        }
        List<JSONDeviceDTO> jsonDeviceDTOList= new ArrayList<>();
        for (Device dev:devices){
            JSONDeviceDTO jsonDeviceDTO=new JSONDeviceDTO();
            jsonDeviceDTO.setId(dev.getId());
            jsonDeviceDTO.setType(dev.getModel().getType().getType());
            jsonDeviceDTO.setModel(dev.getModel().getName());
            jsonDeviceDTO.setInvnum(dev.getInvnum());
            jsonDeviceDTO.setSernum(dev.getSernum());
            jsonDeviceDTO.setCsa(dev.getCsa().getNum());
            jsonDeviceDTOList.add(jsonDeviceDTO);
        }
        System.out.println("devices:= "+devices);
        System.out.println("jsonDeviceDTOList:= "+jsonDeviceDTOList);
        Gson gson = new Gson();
        String jsonDevices = gson.toJson(jsonDeviceDTOList);
        return  ResponseEntity.ok(jsonDevices);
    }

    @GetMapping("/anchDevice")
    public String anchDevice(Model model) {
        System.out.println("GET:/anchDevice...");
        List<TypeDevice> types = typeDeviceRepository.findAll();
        types.remove(0);
        model.addAttribute("types",types);
        List<CSA> csas = csaRepository.findAll();
        model.addAttribute("csas",csas);
        DeviceDTO deviceDTO = new DeviceDTO();
        model.addAttribute(deviceDTO);
        return "anchDevice";
    }

    @PostMapping("/anchDevice")
    public String anchDevice(Model model, @Valid @ModelAttribute DeviceDTO deviceDTO, BindingResult result) {
        System.out.println("POST:/anchDevice...");
        System.out.println("deviceDTO:= "+deviceDTO);
        deviceService.saveDevice(deviceDTO);
//        System.out.println("device:= "+device);
//        device.setCsa(deviceDTO.getCsa());
        model.addAttribute("deviceDTO",deviceDTO);
        return "redirect:/anchDevice";
    }

    @GetMapping("/selDevice")
    public String selDevice(@ModelAttribute OrdersDTO ordersDTO, Model model) {
        System.out.println("Get:/selDevice...");
        System.out.println("ordersDTO:= "+ordersDTO);
        List<TypeDevice> types = typeDeviceRepository.findAll();
        types.remove(0);
        model.addAttribute("types",types);
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

    @GetMapping("/selDeviceCSA")
    public String selDeviceCSA(@ModelAttribute OrdersDTO ordersDTO, Model model) {
        System.out.println("Get:/selDeviceCSA...");
        System.out.println("ordersDTO:= "+ordersDTO);
        List<Device> devices =deviceCSARepository.findByCsa(ordersDTO.getCsa());
        model.addAttribute("ordersDTO",ordersDTO);
        model.addAttribute("devices", devices);
        return "selDeviceCSA";
    }
    @PostMapping("/selDeviceCSA")
    public String selDeviceCSA(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO,
                            BindingResult result, RedirectAttributes atrRedirect){
        System.out.println("POST:/selDeviceCSA...");
        System.out.println("ordersDTO:= "+ordersDTO);
        model.addAttribute("ordersDTO",ordersDTO);
        atrRedirect.addFlashAttribute("ordersDTO",ordersDTO);
        return "redirect:/selDeviceCSA";
    }
    /** событие возникает при нажатии  на кнопку "Создать ТС"
     * на странице device.html.
     * В качестве параметра передается id выбранной модели ТС.
     */
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
    /** событие возникает при нажатии  на кнопку "Добавить"
     * на странице addDevice.html.
     */
    @PostMapping("/addDevice")
    public String addDevice(Model model, @Valid @ModelAttribute DeviceDTO deviceDTO, BindingResult result){
        deviceDTO.setCsa(csaRepository.getById(1));
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
    /** событие возникает при нажатии  на кнопку "Редактировать"
     * на странице device.html.
     * В качестве параметра передается id выбранного ТС.
     */
    @GetMapping("/editDevice{id}")
    public String editDevice(@PathVariable(value ="id") long id, Model model){
        System.out.println("GET:/editDevice{id}...");
        System.out.println("idDev:= "+id);
        Device device=deviceRepository.getById(id);
        System.out.println("device:= "+device);
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getId());
        deviceDTO.setModelDevice(device.getModel());
        deviceDTO.setInvnum(device.getInvnum());
        deviceDTO.setSernum(device.getSernum());
        deviceDTO.setCsa(device.getCsa());
        System.out.println("deviceDTO:=" +deviceDTO);
        model.addAttribute("deviceDTO",deviceDTO);
        return "editDevice";
    }
    /** событие возникает при нажатии  на кнопку "Сохранить"
     * на странице editDevice.html.
     */
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
