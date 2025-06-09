package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.CommentDTO;
import com.example.TrackDevice.DTO.ConsultDTO;
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

    @PostMapping("/consult")
    public String crConsult(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO, BindingResult result) {
        System.out.println("POST:Orders/crConsult...");
        System.out.println("ordersDTO=" +ordersDTO);

        List<CommentDTO> comments= ordersService.getListComments(ordersDTO.getNum());
        ConsultDTO consultDTO = new ConsultDTO();
        consultDTO.setNum(ordersDTO.getNum());
        consultDTO.setIdOrder(ordersDTO.getId());

        model.addAttribute("consultDTO", consultDTO);
        model.addAttribute("comments", comments);

        return "Orders/сonsult";
    }
    @PostMapping("/сomment")
    public String addComment(Model model, @AuthenticationPrincipal UserDetails userDetails,
                            @Valid @ModelAttribute ConsultDTO consultDTO, BindingResult result) {
        System.out.println("POST:Order/comment...");
        System.out.println("consultDTO=" +consultDTO);
        System.out.println("userDetails.getUsername()=" +userDetails.getUsername());

        int i=userDetails.getUsername().indexOf('@');
        String csa =userRepository.findByEmail(userDetails.getUsername()).getCsa().getNum();
        List<CommentDTO> consults;

        System.out.println("consultDTO.getNewComment().isEmpty()="+consultDTO.getNewComment().isEmpty());
        System.out.println("consultDTO.getNewComment()="+consultDTO.getNewComment());


        if(!consultDTO.getNewComment().isEmpty()) {
            String user = userDetails.getUsername().substring(0, i);
            String coment = csa + ":" + user + ":" + consultDTO.getNewComment();
            String subDir = consultDTO.getNum();
            String fileName = "consult_" + consultDTO.getNum() + ".txt";
            String pathFile = fileService.createSubDirCons(subDir, fileName);
            fileService.addComment(pathFile, coment);
            //List<String> consults= fileService.loadConsult(subDir);
            consults = fileService.loadConsult(subDir);
        } else consults=new ArrayList<>();

        model.addAttribute("consultDTO", consultDTO);
        model.addAttribute("consults", consults);
        return "crConsult";
    }
}
