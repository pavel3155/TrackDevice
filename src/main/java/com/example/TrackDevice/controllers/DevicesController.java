package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.DTO.JSONDeviceDTO;
import com.example.TrackDevice.DTO.ModelDeviceDTO;
import com.example.TrackDevice.DTO.TypeDeviceDTO;
import com.example.TrackDevice.Poiji.PoijiDevice;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.TypeDevice;
import com.example.TrackDevice.repo.CSARepository;
import com.example.TrackDevice.repo.DeviceRepository;
import com.example.TrackDevice.repo.ModelDeviceRepository;
import com.example.TrackDevice.repo.TypeDeviceRepository;
import com.example.TrackDevice.service.DeviceService;
import com.example.TrackDevice.service.FileService;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    DeviceRepository deviceRepository;
    @Autowired
    CSARepository csaRepository;
    @Autowired
    TypeDeviceRepository typeDeviceRepository;

    @GetMapping("/types")
    public String viewTypeDevice(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        types.remove(0);
        model.addAttribute("types", types);
        return "Devices/types";
    }
    @GetMapping("/type{id}")
    public String editTypeDevice(@PathVariable(value ="id") long id, Model model){
        System.out.println("id= "+id);
        model.addAttribute("typeDeviceDTO",deviceService.getTypeDeviceDTO(id));
        return "Devices/type";
    }
    @PostMapping("/type")
    public String addTypeDev(Model model, @Valid @ModelAttribute TypeDeviceDTO typeDeviceDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            return "Devices/type";
        }
        try {
            System.out.println("typeDeviceDTO:= "+typeDeviceDTO);
            deviceService.saveTypeDevice(typeDeviceDTO);
            model.addAttribute("typeDeviceDTO",typeDeviceDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("typeDeviceDTO", "name", ex.getMessage()));
        }
        return "Devices/type";
    }

    //загружает страницу с моделями ТС
    @GetMapping("/models")
    public String ModelDevice(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        model.addAttribute("types",types);
        TypeDeviceDTO typeDeviceDTO  = new TypeDeviceDTO();
        model.addAttribute(typeDeviceDTO);
        return "Devices/models";
    }
    //возвращает json -объект(список моделей соответствующих выбранному типу ТС)
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
    //открываем страницу model для создания/редактирования модели ТС
    @GetMapping("/model{idType}&{idModel}")
    public String displayModel(@PathVariable String idType,@PathVariable String idModel, Model model){
        System.out.println("idModel= "+idModel);
        model.addAttribute("modelDeviceDTO",deviceService.getModelDeviceDTO(idType,idModel));
        return "Devices/model";
    }
    //сохраняем новую/измененную модель ТС в БД
    @PostMapping("/model")
    public String saveModel(Model model, @Valid @ModelAttribute ModelDeviceDTO modelDeviceDTO, BindingResult result) {
        //если ошибки есть
        if (result.hasErrors()) {
            if (modelDeviceDTO.getType()==null)
                result.addError(new FieldError("modelDeviceDTO", "type", "тип ТС не может быть null"));
            return "Devices/model";
        }
        try {
            System.out.println("modelDeviceDTO:= "+modelDeviceDTO);
            deviceService.saveModelDevice(modelDeviceDTO);
            model.addAttribute("modelDeviceDTO",modelDeviceDTO);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("modelDeviceDTO", "name", ex.getMessage()));
        }
        return "Devices/model";
    }
    // открываем страницу devices для отображения все ТС
    @GetMapping("/devices")
    public String displayDevices(Model model) {
        List<TypeDevice> types = typeDeviceRepository.findAll();
        types.remove(0);
        model.addAttribute("types",types);
        DeviceDTO deviceDTO = new DeviceDTO();
        model.addAttribute(deviceDTO);
        return "Devices/devices";
    }

    /**
     * возвращает json -объект(список ТС в соответствии с выбранной моделью и установленным фильтром по серийному номеру)
     * @param idModel - модель ТС
     * @param filterSN -серийный номер
     * @return
     */
    @GetMapping("/device{idModel}&{filterSN}")
    @ResponseBody
    public ResponseEntity<String> loadDevices(@PathVariable String idModel, @PathVariable String filterSN){
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
        List<JSONDeviceDTO> jsonDeviceDTOList=deviceService.newObjJSONDeviceDTO(devices);

        System.out.println("devices:= "+devices);
        System.out.println("jsonDeviceDTOList:= "+jsonDeviceDTOList);
        Gson gson = new Gson();
        String jsonDevices = gson.toJson(jsonDeviceDTOList);
        return  ResponseEntity.ok(jsonDevices);
    }


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
        deviceDTO.setId(0);
        System.out.println("deviceDTO:="+deviceDTO);

        model.addAttribute("deviceDTO",deviceDTO);
        model.addAttribute("import",false);
        model.addAttribute("numberEntries",0);
        model.addAttribute("impNumberRecords",0);
        model.addAttribute("notImpNumberRecords",0);

        return "Devices/imp_devices";
    }
    @PostMapping("/imp_devices")
    public String save(@RequestParam("file") MultipartFile[] multipartFiles,
                       @Valid @ModelAttribute DeviceDTO deviceDTO, BindingResult result, Model model) {
        System.out.println("POST:/Devices/imp_devices...");
        System.out.println("deviceDTO:= "+deviceDTO);

        MultipartFile multipartFile = multipartFiles[0];
        int numberEntries=0; //количество записей(ТС) в файле
        int impNumberRecords=0; // количество импортированных записей(ТС)
        int notImpNumberRecords=0; // количество не импортированных записей(ТС)
        boolean impStarted=true;// импорт запущен
        boolean success=true; // импорт выполнен успешно
        boolean error = false; // ошибка
        String txtError ="";
        // если путь к файлу не null
        if (fileService.saveFile(multipartFile,"import","devices")) {
            String locationFile = fileService.getPathFile(multipartFile, "import", "devices");

            try {
                List<PoijiDevice> lstDevices = fileService.getListDevicesFromFileExcel(locationFile).stream()
                        .filter(poijiDevice -> poijiDevice.getModel().equals(deviceDTO.getModelDevice().getName()))
                        .toList();

                numberEntries = lstDevices.size();

                System.out.println("кол-во ТС отобранных для загрузки:= " + lstDevices.size());
                deviceDTO.setStatus("исправный");
                for (PoijiDevice poijiDevice : lstDevices) {
                    deviceDTO.setInvnum(poijiDevice.getInvNum());
                    deviceDTO.setSernum(poijiDevice.getSerNum());

                    try {
                        deviceService.addDevice(deviceDTO);
                        poijiDevice.setSuccess(true);
                        impNumberRecords = impNumberRecords + 1;
                    } catch (DataIntegrityViolationException e) {
                        poijiDevice.setSuccess(false);
                        poijiDevice.setComment("серийный(инвентарный) номер уже существует");
                        notImpNumberRecords = notImpNumberRecords + 1;
                        success = false;
                        System.out.println("poijiDevice:= " + poijiDevice);
                    } catch (Exception ex) {
                        System.out.println("ex:= " + ex);
                        success = false;
                    }

                }
                System.out.println("lstDevices= " + lstDevices);
                List<PoijiDevice> devsNotImp = lstDevices.stream()
                        .filter(poijiDevice -> !(poijiDevice.getSuccess()))
                        .toList();
                System.out.println("devsNotImp= " + devsNotImp);
                model.addAttribute("devsNotImp", devsNotImp);

            } catch (Exception e) {
                System.err.println("Ошибка при чтении файла Excel: " + e.getMessage());
                txtError="Ошибка при чтении файла: " + e.getMessage();
                impStarted=false;
                success = false;
                error=true;

            }
        }
        model.addAttribute("numberEntries",numberEntries);
        model.addAttribute("impNumberRecords",impNumberRecords);
        model.addAttribute("notImpNumberRecords",notImpNumberRecords);
        model.addAttribute("success", success);
        model.addAttribute("import",impStarted);
        model.addAttribute("error",txtError);

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
