package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.Poiji.PoijiDevice;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.repo.CSARepository;
import com.example.TrackDevice.repo.ModelDeviceRepository;
import com.example.TrackDevice.service.DeviceService;
import com.example.TrackDevice.service.FileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/Devices")
public class DevicesController {

    @Autowired
    ModelDeviceRepository modelDeviceRepository;
    @Autowired
    FileService fileService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    CSARepository csaRepository;

    /** обработка события кнопки "Загрузить ТС" на странице device.html.
     * В качестве параметра передается id выбранной модели ТС.
     */
    @GetMapping("/imp_devices{id}")
    public String impDevices(@PathVariable long id,  Model model) {
        System.out.println("GET:/Devices/imp_devices{id}....");
        System.out.println("id= "+id);
        ModelDevice modelDevice = modelDeviceRepository.getById(id);
        System.out.println("modelDevice:= "+modelDevice);
        CSA csa =csaRepository.getByNum("02C001");
        System.out.println("csa:="+ csa);

        DeviceDTO deviceDTO =new DeviceDTO();
        deviceDTO.setModelDevice(modelDevice);
        deviceDTO.setCsa(csa);
//        deviceDTO.setSernum("-");
//        deviceDTO.setInvnum("-");
//        deviceDTO.setStatus("-");
//        deviceDTO.setErr("");
        deviceDTO.setId(0);
        System.out.println("deviceDTO:="+deviceDTO);

        model.addAttribute("deviceDTO",deviceDTO);
        model.addAttribute("import",false);

        return "Devices/imp_devices";
    }
    @PostMapping("/imp_devices")
    public String save(@RequestParam("file") MultipartFile[] multipartFiles,
                       @Valid @ModelAttribute DeviceDTO deviceDTO, BindingResult result, Model model) {
        System.out.println("POST:/Devices/imp_devices...");
        System.out.println("deviceDTO:= "+deviceDTO);

        MultipartFile multipartFile = multipartFiles[0];
        int numberEntries=0;
        int impNumberRecords=0;
        int notImpNumberRecords=0;
        boolean success=true;

        if (fileService.saveFile(multipartFile,"import","devices")){
            String locationFile=fileService.getPathFile(multipartFile,"import","devices");
            List<PoijiDevice> lstDevices= fileService.getListDevicesFromFileExcel(locationFile).stream()
                    .filter(poijiDevice -> poijiDevice.getModel().equals(deviceDTO.getModelDevice().getName()))
                            .toList();

            numberEntries=lstDevices.size();

            System.out.println("кол-во ТС отобранных для загрузки:= "+lstDevices.size());
            deviceDTO.setStatus("исправный");
            for(PoijiDevice poijiDevice : lstDevices){
                deviceDTO.setInvnum(poijiDevice.getInvNum());
                deviceDTO.setSernum(poijiDevice.getSerNum());

                try {
                    deviceService.addDevice(deviceDTO);
                    poijiDevice.setSuccess(true);
                    impNumberRecords=impNumberRecords+1;
                } catch (DataIntegrityViolationException e) {
                    poijiDevice.setSuccess(false);
                    poijiDevice.setComment("серийный(инвентарный) номер уже существует");
                    notImpNumberRecords=notImpNumberRecords+1;
                    success=false;

//                    System.out.println("e.getMessage()={***"+e.getMessage()+"***}");
//                    System.out.println("e.toString()={***"+e.toString()+"***}");
//                    System.out.println("e.getCause()={***"+e.getCause()+"***}");
//                    System.out.println("e.getRootCause()={***"+e.getRootCause()+"***}");
//                    System.out.println("e.getLocalizedMessage()={***"+e.getLocalizedMessage()+"***}");
//                    System.out.println("e.getMostSpecificCause()={***"+e.getMostSpecificCause()+"***}");

                } catch (Exception ex) {
                    success=false;
                }

            }

            List<PoijiDevice> notImpDev= lstDevices.stream()
                    .filter(poijiDevice -> !(poijiDevice.getSuccess()))
                    .toList();
            model.addAttribute("notImpDev",notImpDev);
        }

        model.addAttribute("numberEntries",numberEntries);
        model.addAttribute("impNumberRecords",impNumberRecords);
        model.addAttribute("notImpNumberRecords",notImpNumberRecords);
        model.addAttribute("success", success);
        model.addAttribute("import",true);





        return "Devices/imp_devices";


















//        List<String> listNameFiles=ordersService.getListFileNames(ordersDTO);
//        if (listNameFiles==null)  {
//            System.out.println("....listNameFiles==null");
//            listNameFiles= new ArrayList<>();
//        }
//
//        model.addAttribute("btnCSADisplay",ordersService.btnCSADisplay(ordersDTO));
//        model.addAttribute("btnDeviceDisplay",ordersService.btnSelDeviceDisplay(ordersDTO));
//        model.addAttribute("directory", ordersService.getFileDirectoryOrder(ordersDTO));
//        model.addAttribute("files", listNameFiles);
//        model.addAttribute("execs", ordersService.getListExecs(userDetails));
//        model.addAttribute("restoreMethods", ordersService.getListOfRestoreMethods());
//        model.addAttribute("orderStatus", ordersService.loadStatusOrder());
//        model.addAttribute("actTypes", ordersService.getActTypes());
//        model.addAttribute("ordersDTO",ordersDTO);
//
//
//
//        if (result.hasErrors()) {
//            model.addAttribute("success",false);
//            return "Orders/order";
//        }
//
//        try {
//            Order order=ordersService.save(ordersDTO);
//            ordersDTO.setId(order.getId());
//            System.out.println("ordersService.save(ordersDTO) - выполнено успешно");
//            System.out.println("files.length="+multipartFiles.length);
//            System.out.println("files="+multipartFiles);
//
//            listNameFiles=ordersService.addNewNameFilesInList(listNameFiles,multipartFiles,ordersDTO.getNum());
//            System.out.println("listNameFiles:= "+listNameFiles);
//
//            model.addAttribute("ordersDTO",ordersDTO);
//            model.addAttribute("files", listNameFiles);
//            model.addAttribute("success",true);
//        } catch (Exception ex) {
//            System.out.println("ОШИБКА!!!");
//            result.addError(new FieldError("ordersDTO", "err", ex.getMessage()));
//        }

    }
}
