package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.*;
import com.example.TrackDevice.filter.FilterOrders;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.*;
import com.example.TrackDevice.service.FileService;
import com.example.TrackDevice.service.OrdersService;
import com.example.TrackDevice.specification.OrdersSpecification;
import com.google.gson.Gson;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {
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

    /**
     * Метод обрабатывает запрос GET при преходе на сраницу Orders
     * @param userDetails - с помощью анаотации @AuthenticationPrincipal, получаем данные пользователя
     * @param model - объект класса Model
     * @return - страница Orders
     */
    @GetMapping("/Orders")
    public String Orders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("GET:/Orders...");
        System.out.println("UserName:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());

        Object[] arrRoles=userDetails.getAuthorities().stream().toArray();
        System.out.println("arrRoles[0]= "+arrRoles[0]);
        Object role=arrRoles[0].toString();
        System.out.println("arrRoles[0].toString()="+role);
        List<Order> orders;

        if(role.equals("ROLE_CSA")){
            System.out.println("role.getType()==\"CSA\"");
            User user = userRepository.findByEmail(userDetails.getUsername());
            CSA csa = user.getCsa();
            orders = orderRepository.getByCsa(csa);
        } else if(role.equals("ROLE_EXECDEV")){
            System.out.println("role.getType()==\"EXECDEV\"");
            User executor = userRepository.findByEmail(userDetails.getUsername());
            orders=orderRepository.getByExecutor(executor);
        } else {
            System.out.println("role.getType()==\"SERV, ADMIN\"");
            orders = orderRepository.findAll();
        }

        List<String> orderStatus = ordersService.loadStatusOrder();
        List<CSA> csas = csaRepository.findAll();
        System.out.println("orders:= "+orders);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("csas", csas);
        model.addAttribute("orders",orders);
        FilterOrders filterOrders = new FilterOrders();
        model.addAttribute("filterOrders",filterOrders);

        return "Orders";
    }



    /**
     * метод реализовывает динамический запрос по параметрам объекта filterOrders
     * @param model
     * @param filterOrders
     * @return
     */
    @PostMapping("/FilterOrders")
    public String filterOrders(Model model, @AuthenticationPrincipal UserDetails userDetails,@Valid @ModelAttribute FilterOrders filterOrders) {
        System.out.println("POST:/FilterOrders...");
        System.out.println("filterOrders= "+filterOrders);
        System.out.println("UserName:= "+userDetails.getUsername());
        Object role=ordersService.getRoleFromUserDetails(userDetails);

        Specification<Order> spec = Specification.where(null);

        if(role.equals("ROLE_CSA")){
            System.out.println("role.getType()==\"CSA\"");
            CSA csa = userRepository.findByEmail(userDetails.getUsername()).getCsa();
            spec = spec.and(ordersSpecification.hasCSA(csa));
        } else if(role.equals("ROLE_EXECDEV")){
            System.out.println("role.getType()==\"EXECDEV\"");
            User executor = userRepository.findByEmail(userDetails.getUsername());
            spec = spec.and(ordersSpecification.hasExecutor(executor));
        } else {
            System.out.println("role.getType()==\"SERV, ADMIN\"");
            if (!"---".equals(filterOrders.getCsa().getNum())) spec = spec.and(ordersSpecification.hasCSA(filterOrders.getCsa()));
        }

        if (!filterOrders.getStartDate().isEmpty() && !filterOrders.getEndDate().isEmpty()) {
            LocalDate startDate = ordersService.toLocalData(filterOrders.getStartDate());
            LocalDate endDate = ordersService.toLocalData(filterOrders.getEndDate());
            spec = spec.and(ordersSpecification.dateBetween(startDate,endDate));
        }
        if (!filterOrders.getNum().isEmpty()) spec = spec.and(ordersSpecification.hasNum(filterOrders.getNum()));
        if (!"---".equals(filterOrders.getStatus())) spec = spec.and(ordersSpecification.hasStatus(filterOrders.getStatus()));

        var orders = orderRepository.findAll(spec);

        System.out.println("orders= "+orders);
        List<String> orderStatus = ordersService.loadStatusOrder();
        List<CSA> csas = csaRepository.findAll();

        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("csas", csas);
        model.addAttribute("orders",orders);
        model.addAttribute("filterOrders",filterOrders);

        return "Orders";
    }
    @PostMapping("/addComment")
    public String addComent(Model model, @AuthenticationPrincipal UserDetails userDetails,
                            @Valid @ModelAttribute ConsultDTO consultDTO, BindingResult result) {
        System.out.println("POST:addComent...");
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
    @PostMapping("/crConsult")
    public String crConsult(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO, BindingResult result) {
        System.out.println("POST:crConsult...");
        System.out.println("ordersDTO=" +ordersDTO);

        List<CommentDTO> consults= fileService.loadConsult(ordersDTO.getNum());
        ConsultDTO consultDTO = new ConsultDTO();
        consultDTO.setNum(ordersDTO.getNum());
        consultDTO.setIdOrder(ordersDTO.getId());

        model.addAttribute("consultDTO", consultDTO);
        model.addAttribute("consults", consults);

        return "crConsult";
    }



    /**
     * Метод отрабатывает переход на страницу 'addOrder' :
     *  -при нажатии 'Создать заявку' на странице Orders
     *  -при выполнении "redirect:/addOrder" - @PostMapping("/addOrder")
     * @param ordersDTO
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("/addOrder")
    public String Order(@ModelAttribute OrdersDTO ordersDTO,
                        @AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("GET:/addOrder....");
        System.out.println("ordersDTO:= "+ordersDTO);

        System.out.println("Username:= "+userDetails.getUsername());
        System.out.println("Role:= "+userDetails.getAuthorities());


        if (ordersDTO==null){
            System.out.println("ordersDTO==null");
            ordersDTO=new OrdersDTO();
        }

        if (ordersDTO.getCsa()==null){
            System.out.println("ordersDTO.getCsa()==null....");

            Roles roleCSA =roleRepository.findByType("CSA");
            System.out.println("roleCSA:= "+roleCSA);

            if(userDetails.getAuthorities().contains(roleCSA))
            //если рользователь с ролью CSA
            {
                CSA csa= userRepository.findByEmail(userDetails.getUsername()).getCsa();//получаем csa(объект) из пользователя
                System.out.println("csa:="+csa);
                ordersDTO.setCsa(csa);//присваиваем свойству csa объекта ordersDTO, полученный csa пользователя
                ordersDTO.setIdCSA(csa.getId()); //присваиваем свойству idCsa объекта ordersDTO, id полученного объекта csa пользователя
            }else{
                CSA csa = csaRepository.getById(1); // получаем csa '---'
                ordersDTO.setCsa(csa);
                ordersDTO.setIdCSA(1);
            }
        }

        if (ordersDTO.getDevice() ==null){
            //получаем device "---"
            System.out.println("ordersDTO.getDevice()==null....");
            List<Device> devices = deviceRepository.findBySernum("---");
            Device device=devices.get(0);
            ordersDTO.setDevice(device);
            ordersDTO.setIdDevice(device.getId());
        }

        if (ordersDTO.getStatus() ==null){
            ordersDTO.setStatus("открыта");
        }
        //полуаем список пользователей с ролью "ROLE_SERV",
        // выбираем первого из списка и присваиваем его сойству Executor объекту ordersDTO
        if(ordersDTO.getExecutor()==null){
            System.out.println("ordersDTO.getExecutor()==null....");
            Roles role=roleRepository.findByRole("ROLE_SERV");
            System.out.println("role:= "+role);
            List<User> execs =userRepository.findByRole(role);//получаем всех пользователей с ролью "ROLE_SERV"
            System.out.println("execs:= "+execs);
            User exec = execs.get(0);//получаем первого пользователя из списка
            System.out.println("exec:= "+exec);
            ordersDTO.setExecutor(exec);
        }

        //если авторизованый пользователь с ролью "EXECDEV"-исполнитель,
        //то список "execs" будет содержать только одного этого пользователя
        if(userDetails.getAuthorities().contains(roleRepository.findByType("EXECDEV"))) {
            List<User> execs = new ArrayList<>();
            execs.add(userRepository.findByEmail(userDetails.getUsername()));
            System.out.println("user: EXECDEV...");
            System.out.println("execs:= " +execs);
            model.addAttribute("execs", execs);
        }
        //если авторизованый пользователь с ролью "SERV",
        //то список "execs" будет содержать пользователей с ролью "ROLE_EXECDEV"  и "ROLE_SERV"
        if(userDetails.getAuthorities().contains(roleRepository.findByType("SERV"))) {
            List<Roles> roles = new ArrayList<>();
            roles.add(roleRepository.findByRole("ROLE_EXECDEV"));
            roles.add(roleRepository.findByRole("ROLE_SERV"));
            List<User> execs = userRepository.findByRoleIn(roles);
            System.out.println("execs:= " +execs);
            model.addAttribute("execs", execs);
        }

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
        return "addOrder";
    }

    /**
     * Технический метод, используется  для добавления объектов CSA на странице "/selCSA" и
     * Device  на странице "/selDevice" в объект ordersDTO и дальнейшей передачи его в формы
     * страниц "/addOrder" и "/editOrder"
     * @param model
     * @param ordersDTO
     * @param result
     * @param atrRedirect
     * @return
     */
    @PostMapping("/addOrder/Prop")
    public String Order(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO,
                           BindingResult result, RedirectAttributes atrRedirect) {
        System.out.println("POST:/addOrder/Prop...");
        System.out.println("ordersDTO:= "+ordersDTO);
        ordersDTO.setCsa(csaRepository.getById(ordersDTO.getIdCSA()));
        ordersDTO.setDevice(deviceRepository.getById(ordersDTO.getIdDevice()));
        System.out.println("POST_/addOrder_ordersDTO:= "+ordersDTO);
        atrRedirect.addFlashAttribute("ordersDTO",ordersDTO);
        if(ordersDTO.getId()!=0){
            System.out.println("ordersDTO.getId():= "+ordersDTO.getId());
            return "redirect:/editOrder";
        }
    return "redirect:/addOrder";
    }

    /**
     * метод выполняется при нажатии на кнопку "Добавить" на странице addOrder
     * @param files
     * @param model
     * @param ordersDTO
     * @param result
     * @return
     */
    @PostMapping("/addOrder")
    public String addOrder(@RequestParam("files") MultipartFile[] files,
                           Model model, @Valid @ModelAttribute OrdersDTO ordersDTO,
                           BindingResult result) {
        System.out.println("POST:/addOrder/Add...");
        System.out.println("ordersDTO:= "+ordersDTO);

        List<String> fileNames=new ArrayList<>();

        List<Roles> roles = new ArrayList<>();
        roles.add(roleRepository.findByRole("ROLE_EXECDEV"));
        roles.add(roleRepository.findByRole("ROLE_SERV"));
        List<User> execs =userRepository.findByRoleIn(roles);
        List<Restore> restoreMethods=restoreRepository.findAll();

        model.addAttribute("restoreMethods", restoreMethods);
        model.addAttribute("execs", execs);
        model.addAttribute("ordersDTO",ordersDTO);

        if (result.hasErrors()) {
            model.addAttribute("directory", "");
            model.addAttribute("files", fileNames);
            model.addAttribute("success",false);
            return "addOrder";
        }
        try {
            ordersService.add(ordersDTO);
            for (MultipartFile file : files) {
                if (fileService.saveFile(file, ordersDTO.getNum())) {
                    fileNames.add(file.getOriginalFilename());
                }
            }
            model.addAttribute("files", fileNames);
            model.addAttribute("directory", ordersDTO.getNum());
            model.addAttribute("success", true);
        } catch (DataIntegrityViolationException e) {
            result.addError(new FieldError("ordersDTO", "num", "заявка с таким номером уже существует"));
        } catch (Exception ex) {
            result.addError(new FieldError("ordersDTO", "num", ex.getMessage()));
        }

        return "addOrder";
    }

    /**
     * метод выполняет скачивание с сервера прикрепленных к заявке изображений  на локальный диск
     * @param direc
     * @param fileName
     * @param model
     * @return
     */
    @GetMapping("/addOrder/downloadPicture")
    public ResponseEntity<Resource> downloadPicture(@RequestParam String direc, @RequestParam String fileName,  Model model) {
        System.out.println("GET:/addOrder/downloadPicture...");
        System.out.println("directory:= " + direc);
        System.out.println("fileName:= " + fileName);

        Resource resource = fileService.loadFile(direc, fileName);
        String encFileName = fileService.encodeFile(fileName);
        if (!encFileName.isEmpty()) {
            System.out.println("encFileName= " + encFileName);
            String contentDisposition = String.format("attachment; filename*=UTF-8''%s", encFileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
        } else return ResponseEntity.internalServerError().build();

    }

    /**
     * метод выполняет функцию просмотра прикрепленных к заявке изображений  на локальный диск
     * @param direc
     * @param fileName
     * @return
     */
    @GetMapping("/addOrder/loadPicture")
    public ResponseEntity<Resource> loadPicture(@RequestParam String direc, @RequestParam String fileName) {
        System.out.println("GET:/addOrder/loadPicture...");
        System.out.println("directory:= " + direc);
        System.out.println("fileName:= " + fileName);

        Resource resource = fileService.loadFile(direc, fileName);
        if (resource!=null) {
            String fileExtension = getFileExtencion(fileName);
            if (fileExtension.equals("jpg")){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else  if (fileExtension.equals("pdf")){
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.ALL)
                        .body(resource);
                }
        } else return ResponseEntity.internalServerError().build();

    }

    /**
     * метод взвращает расширение файла
     * @param fileName
     * @return
     */
    private String getFileExtencion(String fileName){
        if (fileName==null){
            return  null;
        }
        int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex>=0){
            return fileName.substring(dotIndex+1);
        }
        return "";
    }

    /**
     * метод выполняет удаление с сервера прикрепленных к заявке изображений
     * @param direc
     * @param fileName
     * @return
     */
    @GetMapping("/addOrder/deletPicture{direc}&{fileName}")
    public ResponseEntity<String> delPicture(@PathVariable String direc, @PathVariable String fileName) {
        System.out.println("GET:/addOrder/delPicture...");
        System.out.println("directory:= " + direc);
        System.out.println("fileName:= " + fileName);
        fileService.delFile(direc, fileName);
        return ResponseEntity.ok("Файл удален");
    }

    /**
     * метод генерирует и возвращает номер заявки
     * @param OrderDate
     * @return
     */
    @GetMapping("/GenerationNumOrder{OrderDate}")
    @ResponseBody
    public ResponseEntity<String> generationNumOrder(@PathVariable String OrderDate) {
        System.out.println("GET:/GenerationNumOrder{OrderDate}...");
        System.out.println("OrderDate:= " + OrderDate);
        List<Order> orders = orderRepository.findAllByNumStartingWith(OrderDate);
        String num= ordersService.GenerationNumOrder(orders);
        String numOrder = OrderDate+num;
        Gson gson = new Gson();
        String jsonNumOrder = gson.toJson(numOrder);
        return  ResponseEntity.ok(jsonNumOrder);
    }

    /**
     * метод осуществляет выбор КСА
     * @param csa_id
     * @param model
     * @return
     */
    @GetMapping("/addOrder/selCSA")
    public String selCSA(@RequestParam(value ="idCSA") long csa_id, Model model){
        System.out.println("idCSA= "+csa_id);
        CSA csa=csaRepository.getById(csa_id);
        System.out.println("/addOrder/selCSA{id}...");
        System.out.println("csa:=" +csa);
        OrdersDTO ordersDTO = new OrdersDTO();
        if (csa!=null){
            ordersDTO.setCsa(csa);
        }
        model.addAttribute("ordersDTO",ordersDTO);
        return "redirect:/addOrder";
    }

    /**
     * метод загружает страницу editOrder, заполняет ее параметрами
     * @param ordersDTO
     * @param model
     * @return
     */
    @GetMapping("/editOrder")
    public String editOrder(@ModelAttribute OrdersDTO ordersDTO, Model model) {
        System.out.println("GET:/editOrder....");
        System.out.println("ordersDTO:= "+ordersDTO);

        List<Roles> roles = new ArrayList<>();
        roles.add(roleRepository.findByRole("ROLE_EXECDEV"));
        roles.add(roleRepository.findByRole("ROLE_SERV"));
        List<User> execs =userRepository.findByRoleIn(roles);
        System.out.println("execs:= " +execs);
        System.out.println("ordersDTO:= " +ordersDTO);
        List<Restore> restoreMethods=restoreRepository.findAll();
        model.addAttribute("restoreMethods", restoreMethods);
        model.addAttribute("execs", execs);
        model.addAttribute("ordersDTO", ordersDTO);

        boolean selDevOrder= ordersService.btnSelDeviceDisplay(ordersDTO.getCsa(),ordersDTO.getDevice());
        model.addAttribute("selDevOrder", selDevOrder);
        return "editOrder";
    }

    /** метод выполняется при нажатии на кнопку Редактировать на странице Orders
     * метод принимает id заявки, загружает страницу editOrder
     */
    @GetMapping("/editOrder{id}")
    public String editOrder(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("GET:/editOrder{id}....");
        Order order = orderRepository.getById(id);
        System.out.println("order:= "+order);


        boolean selDevOrder= ordersService.btnSelDeviceDisplay(order.getCsa(),order.getDevice());
        model.addAttribute("selDevOrder", selDevOrder);
        OrdersDTO ordersDTO =ordersService.newOrderDTOtoObject(order);

        Object[] arrRoles=userDetails.getAuthorities().stream().toArray();
        Object role=arrRoles[0].toString();
        List<User> execs;
        List<Roles> roles = new ArrayList<>();

        if(role.equals("ROLE_SERV")){
            roles.add(roleRepository.findByRole("ROLE_EXECDEV"));
            roles.add(roleRepository.findByRole("ROLE_SERV"));
            execs =userRepository.findByRoleIn(roles);
        } else {
            execs=new ArrayList<>();
            execs.add(ordersDTO.getExecutor());
        }

        List<Restore> restoreMethods=restoreRepository.findAll();
        model.addAttribute("restoreMethods", restoreMethods);

        List<String> fileNames;
        String directory;
        if (ordersDTO.getNum()!=null) {
            System.out.println("ordersDTO.getNum()!=null...");
            fileNames = fileService.getAllFiles(ordersDTO.getNum(),"pic");
            directory=ordersDTO.getNum();
        } else {
            fileNames=new ArrayList<>();
            directory="";
        }

        List<ActTypes> actTypes = actTypesRepository.findAll();
        actTypes.remove(0);
        List<String> orderStatus = ordersService.loadStatusOrder();


        System.out.println("directory:="+directory);
        System.out.println("fileNames:= " +fileNames);
        System.out.println("execs:= " + execs);
        System.out.println("ordersDTO:= " +ordersDTO);

        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("actTypes", actTypes);
        model.addAttribute("directory", directory);
        model.addAttribute("files", fileNames);
        model.addAttribute("execs", execs);
        model.addAttribute("ordersDTO", ordersDTO);
        return "editOrder";
    }
    @PostMapping("/editOrder")
    public String editOrder(@RequestParam("files") MultipartFile[] files,
                            @Valid @ModelAttribute OrdersDTO ordersDTO, BindingResult result, Model model) {
        System.out.println("POST:/editOrder...");
        System.out.println("ordersDTO:= "+ordersDTO);

        boolean selDevOrder= ordersService.btnSelDeviceDisplay(ordersDTO.getCsa(),ordersDTO.getDevice());
        model.addAttribute("selDevOrder", selDevOrder);

        List<String> fileNames;
        if(fileService.getAllFiles(ordersDTO.getNum(),"pic")!=null){
            fileNames = fileService.getAllFiles(ordersDTO.getNum(),"pic");
        } else{
            fileNames = new ArrayList<>();
        }

        System.out.println("fileNames="+fileNames);

        List<Roles> roles = new ArrayList<>();
        roles.add(roleRepository.findByRole("ROLE_EXECDEV"));
        roles.add(roleRepository.findByRole("ROLE_SERV"));
        List<User> execs =userRepository.findByRoleIn(roles);
        List<Restore> restoreMethods=restoreRepository.findAll();
        List<ActTypes> actTypes = actTypesRepository.findAll();
        List<String> orderStatus = new ArrayList<>();
        orderStatus.add("---");
        orderStatus.add("открыта");
        orderStatus.add("закрыта");

        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("directory", ordersDTO.getNum());
        model.addAttribute("files", fileNames);
        model.addAttribute("restoreMethods", restoreMethods);
        model.addAttribute("execs", execs);
        model.addAttribute("actTypes",actTypes);
        model.addAttribute("ordersDTO",ordersDTO);

        if (result.hasErrors()) {
            model.addAttribute("success",false);
            return "editOrder";
        }
        try {
            ordersService.save(ordersDTO);
            System.out.println("ordersService.save(ordersDTO) - выполнено успешно");
            System.out.println("files.length="+files.length);
            System.out.println("files="+files);
            for (MultipartFile file : files) {
               if (fileService.saveFile(file,ordersDTO.getNum())){
                   System.out.println("file.getOriginalFilename()="+file.getOriginalFilename());
                   fileNames.add(file.getOriginalFilename());
                   System.out.println("fileNames="+fileNames);
               }
            }
            model.addAttribute("files", fileNames);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("ordersDTO", "err", ex.getMessage()));
        }
        return "/editOrder";
    }
}

