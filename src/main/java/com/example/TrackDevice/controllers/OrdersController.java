package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.filter.FilterOrders;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.*;
import com.example.TrackDevice.service.FileService;
import com.example.TrackDevice.service.OrdersService;
import com.example.TrackDevice.specification.OrdersSpecification;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
     * Метод обрабатывает запрос GET при преходе на сраницу Orders
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
     *  -при нажатии 'Создать заявку' на странице orders
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("order{id}")
    public String Order(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("GET:/order{id}....");
        System.out.println("id= "+id);
        System.out.println("Username:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());

        OrdersDTO ordersDTO =ordersService.objOrderDTOCreate(id,userDetails);
        System.out.println("ordersDTO:= " +ordersDTO);
        model.addAttribute("directory", ordersService.getFileDirectoryOrder(ordersDTO));
        model.addAttribute("files", ordersService.getListFileNames(ordersDTO));
        model.addAttribute("execs", ordersService.getListExecs(userDetails));
        model.addAttribute("restoreMethods", ordersService.getListOfRestoreMethods());
        model.addAttribute("ordersDTO",ordersDTO);

//
//
//        model.addAttribute("directory", directory);
//        model.addAttribute("files", fileNames);
//        model.addAttribute("restoreMethods", restoreMethods);
//        model.addAttribute("ordersDTO", ordersDTO);
        return "Orders/order";
}
    @PostMapping("/order")
    public String objTransferToOrder(Model model,
                                     @Valid @ModelAttribute OrdersDTO ordersDTO,
                                     @AuthenticationPrincipal UserDetails userDetails){
        System.out.println("POST:/order....");
        System.out.println("Username:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());
        System.out.println("ordersDTO:= "+ordersDTO);

        ordersDTO.setCsa(csaRepository.getById(ordersDTO.getIdCSA()));
        ordersDTO.setDevice(deviceRepository.getById(ordersDTO.getIdDevice()));

//        if (ordersDTO.getCsa()==null) {
//            System.out.println("ordersDTO.getCsa()==null....");
//            //присваиваем свойству csa объекта ordersDTO, полученный csa пользователя
//            ordersDTO.setCsa(ordersService.getCSAfromUserDetails(userDetails));
//            //присваиваем свойству idCsa объекта ordersDTO, id полученного объекта csa пользователя
//            ordersDTO.setIdCSA(ordersService.getCSAfromUserDetails(userDetails).getId());
//        }
//
//        if (ordersDTO.getDevice() ==null){
//            //устанавливаем значение device "---"
//            ordersDTO.setDevice(ordersService.getDeviceDefault());
//            //устанавливаем значение id объекта device "---"
//            ordersDTO.setIdDevice(ordersService.getDeviceDefault().getId());
//        }

////        if (ordersDTO.getStatus() ==null){
////            ordersDTO.setStatus("открыта");
////        }
//        ordersDTO.setStatus("открыта");//устанавливаем статус заявки



        //полуаем список пользователей с ролью "ROLE_SERV",
        // выбираем первого из списка и присваиваем его сойству Executor объекту ordersDTO
        if(ordersDTO.getExecutor()==null){
            ordersDTO.setExecutor(ordersService.getExecutorDefault());
        }

        //формируем список исполнителей "execs"
        List<User> execs=ordersService.getListExecs(userDetails);
        model.addAttribute("execs", execs);


        if (ordersDTO.getRestore()==null){
            ordersDTO.setRestore(restoreRepository.getByMethod("---"));
        }
        List<Restore> restoreMethods=restoreRepository.findAll();

        List<String> fileNames;
        String directory;
        if (ordersDTO.getId()!=0) {
            System.out.println("ordersDTO.getNum()!=null...");
            fileNames = fileService.getAllFiles(ordersDTO.getNum(),"pic");
            directory=ordersDTO.getNum();
            System.out.println("directory:="+directory);
        } else {
            fileNames=new ArrayList<>();
            directory="";
        }

        System.out.println("ordersDTO:= " +ordersDTO);
        System.out.println("fileNames:= " +fileNames);
        model.addAttribute("directory", directory);
        model.addAttribute("files", fileNames);
        model.addAttribute("restoreMethods", restoreMethods);
        model.addAttribute("ordersDTO", ordersDTO);
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



}
