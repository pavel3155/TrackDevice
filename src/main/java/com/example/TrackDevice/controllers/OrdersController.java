package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.CommentDTO;
import com.example.TrackDevice.DTO.ConsultDTO;
import com.example.TrackDevice.DTO.MessageDTO;
import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.filter.FilterOrders;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.*;
import com.example.TrackDevice.service.FileService;
import com.example.TrackDevice.service.OrdersService;
import com.example.TrackDevice.specification.OrdersSpecification;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Orders")
public class OrdersController {
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
    @Autowired
    RestoreRepository restoreRepository;
    @Autowired
    ActTypesRepository actTypesRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private OrdersSpecification ordersSpecification;
    @Autowired
    TypeDeviceRepository typeDeviceRepository;

    /**
     * Метод выводит сохраненные заявки на сранице Orders
     * @param userDetails - анаотация @AuthenticationPrincipal позволяет получить данные пользователя
     * @param model - объект класса Model
     * @return - страница orders
     */
    @GetMapping("/orders")
    public String Orders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("GET:/Orders/orders...");
        System.out.println("UserName:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());
        Object role=ordersService.getRoleFromUserDetails(userDetails);
        List<Order> orders = ordersService.getListOfUsersOrders(role,userDetails);
        List<String> orderStatus = ordersService.loadStatusOrder();
        List<CSA> csas = csaRepository.findAll();
        System.out.println("orders:= "+orders);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("csas", csas);
        model.addAttribute("orders",orders);
        FilterOrders filterOrders = new FilterOrders();
        model.addAttribute("filterOrders",filterOrders);
        return "Orders/orders";
    }

    /**
     * Метод отрабатывает переход на страницу 'order' :
     *  -при нажатии 'Создать заявку' или 'Открыть' на странице orders
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("order{id}")
    public String Order(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("GET:/Orders/order{id}....");
        System.out.println("id= "+id);
        System.out.println("Username:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());

        OrdersDTO ordersDTO =ordersService.objOrderDTOCreate(id,userDetails);
        System.out.println("ordersDTO:= " +ordersDTO);
        model.addAttribute("btnCSADisplay",ordersService.btnCSADisplay(ordersDTO));
        model.addAttribute("btnDeviceDisplay",ordersService.btnSelDeviceDisplay(ordersDTO));
        model.addAttribute("directory", ordersService.getFileDirectoryOrder(ordersDTO));
        model.addAttribute("files", ordersService.getListFileNames(ordersDTO));
        model.addAttribute("execs", ordersService.getListExecs(userDetails));
        model.addAttribute("restoreMethods", ordersService.getListOfRestoreMethods());
        model.addAttribute("orderStatus", ordersService.loadStatusOrder());
        model.addAttribute("actTypes", ordersService.getActTypes());
        model.addAttribute("ordersDTO",ordersDTO);

        return "Orders/order";
}


    @PostMapping("/saveOrder")
    public String save(@RequestParam("files") MultipartFile[] addFiles,
                       @AuthenticationPrincipal UserDetails userDetails,
                       @Valid @ModelAttribute OrdersDTO ordersDTO, BindingResult result, Model model) {
        System.out.println("POST:/saveOrder...");
        System.out.println("ordersDTO:= "+ordersDTO);

//        boolean selDevOrder= ordersService.btnSelDeviceDisplay(ordersDTO.getCsa(),ordersDTO.getDevice());
//        model.addAttribute("selDevOrder", selDevOrder);

//        List<String> fileNames;
//        if(fileService.getAllFiles(ordersDTO.getNum(),"pic")!=null){
//            fileNames = fileService.getAllFiles(ordersDTO.getNum(),"pic");
//        } else{
//            fileNames = new ArrayList<>();
//        }
//
//        System.out.println("fileNames="+fileNames);

//        List<Roles> roles = new ArrayList<>();
//        roles.add(roleRepository.findByRole("ROLE_EXECDEV"));
//        roles.add(roleRepository.findByRole("ROLE_SERV"));
//        List<User> execs =userRepository.findByRoleIn(roles);
//        List<Restore> restoreMethods=restoreRepository.findAll();
//        List<ActTypes> actTypes = actTypesRepository.findAll();
//        List<String> orderStatus = new ArrayList<>();
//        orderStatus.add("---");
//        orderStatus.add("открыта");
//        orderStatus.add("закрыта");

//        model.addAttribute("orderStatus", orderStatus);
//        model.addAttribute("directory", ordersDTO.getNum());
//        model.addAttribute("files", fileNames);
//        model.addAttribute("restoreMethods", restoreMethods);
//        model.addAttribute("execs", execs);
//        model.addAttribute("actTypes",actTypes);
//        model.addAttribute("ordersDTO",ordersDTO);

        List<String> files=ordersService.getListFileNames(ordersDTO);

        model.addAttribute("btnCSADisplay",ordersService.btnCSADisplay(ordersDTO));
        model.addAttribute("btnDeviceDisplay",ordersService.btnSelDeviceDisplay(ordersDTO));
        model.addAttribute("directory", ordersService.getFileDirectoryOrder(ordersDTO));
        model.addAttribute("files", files);
        model.addAttribute("execs", ordersService.getListExecs(userDetails));
        model.addAttribute("restoreMethods", ordersService.getListOfRestoreMethods());
        model.addAttribute("orderStatus", ordersService.loadStatusOrder());
        model.addAttribute("actTypes", ordersService.getActTypes());
        model.addAttribute("ordersDTO",ordersDTO);



        if (result.hasErrors()) {
            model.addAttribute("success",false);
            return "editOrder";
        }
        try {
            ordersService.save(ordersDTO);
            System.out.println("ordersService.save(ordersDTO) - выполнено успешно");
            System.out.println("files.length="+addFiles.length);
            System.out.println("files="+files);
            for (MultipartFile file : addFiles) {
                if (fileService.saveFile(file,ordersDTO.getNum())){
                    System.out.println("file.getOriginalFilename()="+file.getOriginalFilename());
                    files.add(file.getOriginalFilename());
                    System.out.println("fileNames="+files);
                }
            }
            model.addAttribute("files", files);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("ordersDTO", "err", ex.getMessage()));
        }
        return "/editOrder";
    }

    @PostMapping("/order")
    public String objTransferToOrder(Model model,
                                     @Valid @ModelAttribute OrdersDTO ordersDTO,
                                     @AuthenticationPrincipal UserDetails userDetails){
        System.out.println("POST:Orders/order....");
        System.out.println("Username:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());
        System.out.println("ordersDTO:= "+ordersDTO);


        boolean csaChanges=false;
        boolean  belongs=true;

        Device device =ordersService.getDevice(ordersDTO.getIdDevice());
        if (!ordersDTO.getNum().equals("---")&&
                ordersDTO.getIdCSA()!=ordersDTO.getCsa().getId()){
            csaChanges=true;
            ordersDTO.setCsa(ordersService.getCSA(ordersDTO.getIdCSA()));
            belongs= ordersService.DeviceBelongsThisCSA(ordersDTO.getIdCSA(),device.getCsa().getId());
        }

        if (belongs){
            ordersDTO.setDevice(device);
        } else {
            ordersDTO.setDevice(ordersService.getDeviceDefault());
        }


        model.addAttribute("btnCSADisplay",ordersService.btnCSADisplay(ordersDTO));
        model.addAttribute("btnDeviceDisplay",ordersService.btnSelDeviceDisplay(ordersDTO));
        model.addAttribute("directory", ordersService.getFileDirectoryOrder(ordersDTO));
        model.addAttribute("files", ordersService.getListFileNames(ordersDTO));
        model.addAttribute("execs", ordersService.getListExecs(userDetails));
        model.addAttribute("restoreMethods", ordersService.getListOfRestoreMethods());
        model.addAttribute("orderStatus", ordersService.loadStatusOrder());
        model.addAttribute("actTypes", ordersService.getActTypes());
        model.addAttribute("ordersDTO",ordersDTO);

        return "Orders/order";
    }

    /**
     * метод обрабатывает переход на страницу выбора CSA
     * @param model
     * @param ordersDTO-передается со странцы order
     * @return
     */
    @PostMapping("/csa")
    public String selCSA(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO){
        System.out.println("POST:/Orders/csa...");
        System.out.println("ordersDTO:= "+ordersDTO);
        //получаем список с кодами регионов
        List<String> codes = csaRepository.findDistinctCode();
        model.addAttribute("codes",codes);
        model.addAttribute("ordersDTO",ordersDTO);
        CSA csa  = new CSA();
        model.addAttribute(csa);
        return "Orders/csa";
    }
    @PostMapping("/device")
    public String selDevice(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO){
        System.out.println("POST:/Orders/device...");
        System.out.println("ordersDTO:= "+ordersDTO);
        List<TypeDevice> types = typeDeviceRepository.findAll();
        types.remove(0);
        model.addAttribute("types",types);
        model.addAttribute("ordersDTO",ordersDTO);
        return "Orders/device";
    }
    /**
     * метод генерирует и возвращает номер заявки
     * @param OrderDate
     * @return
     */
    @GetMapping("/NumOrder{OrderDate}")
    @ResponseBody
    public ResponseEntity<String> generationNumOrder(@PathVariable String OrderDate) {
        System.out.println("GET:/NumOrder{OrderDate}...");
        System.out.println("OrderDate:= " + OrderDate);
        String numOrder = ordersService.numOrder(OrderDate);
        Gson gson = new Gson();
        String jsonNumOrder = gson.toJson(numOrder);
        return  ResponseEntity.ok(jsonNumOrder);
    }

    @PostMapping("/messages")
    public String messages(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO, BindingResult result) {
        System.out.println("POST:Orders/messages...");
        System.out.println("ordersDTO=" +ordersDTO);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setNum(ordersDTO.getNum());
        messageDTO.setIdOrder(ordersDTO.getId());

        model.addAttribute("messageDTO", messageDTO);
        model.addAttribute("messages", ordersService.getListMessages(ordersDTO.getNum()));

        return "Orders/messages";
    }
    @PostMapping("/addMessage")
    public String addMessage(Model model, @AuthenticationPrincipal UserDetails userDetails,
                            @Valid @ModelAttribute MessageDTO messageDTO, BindingResult result) {
        System.out.println("POST:Order/addMessage...");
        System.out.println("messageDTO=" +messageDTO);
        System.out.println("userDetails.getUsername()=" +userDetails.getUsername());
        System.out.println("consultDTO.getNewComment().isEmpty()="+messageDTO.getMessage().isEmpty());
        System.out.println("consultDTO.getNewComment()="+messageDTO.getMessage());

        model.addAttribute("messages", ordersService.addMessage(userDetails,messageDTO));
        model.addAttribute("messageDTO", messageDTO);
        return "Orders/messages";
    }
}
