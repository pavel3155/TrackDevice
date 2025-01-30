package com.example.TrackDevice.controllers;

import com.example.TrackDevice.DTO.*;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.*;
import com.example.TrackDevice.service.FileService;
import com.example.TrackDevice.service.OrdersService;
import jakarta.validation.Valid;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

import java.util.ArrayList;
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

        }else if(role.equals("ROLE_EXECDEV")){
            System.out.println("role.getType()==\"EXECDEV\"");
            User executor = userRepository.findByEmail(userDetails.getUsername());
            orders=orderRepository.getByExecutor(executor);
        } else {
            System.out.println("role.getType()==\"SERV, ADMIN\"");
            orders = orderRepository.findAll();
        }

//        Roles roleCSA =roleRepository.findByType("CSA");
//        System.out.println("roleCSA:= "+roleCSA);
//        List<Order> orders;
//        if(userDetails.getAuthorities().contains(roleCSA)){
//            User user = userRepository.findByEmail(userDetails.getUsername());
//            System.out.println("user:="+user);
//            CSA csa = user.getCsa();
//            System.out.println("csa:= "+csa);
//            orders = orderRepository.getByCsa(csa);
//        } else{
//            orders = orderRepository.findAll();
//        }

        List<CSA> csas = csaRepository.findAll();
        System.out.println("orders:= "+orders);
        model.addAttribute("csas", csas);
        model.addAttribute("orders",orders);
        return "Orders";
    }

    @PostMapping("/Orders")
    public String Orders(Model model, @Valid @ModelAttribute OrdersDTO ordersDTO, BindingResult result) {

        //если ошибки есть
        if (result.hasErrors()) {
            List<CSA> csas = csaRepository.findAll();
            List<Device> devices = deviceRepository.findAll();
            model.addAttribute("csas", csas);
            model.addAttribute("devices", devices);
            return "regUser";
        }
        try {
            ordersService.add(ordersDTO);
            List<CSA> csas = csaRepository.findAll();
            List<Device> devices = deviceRepository.findAll();
            model.addAttribute("csas", csas);
            model.addAttribute("devices", devices);
            model.addAttribute("ordersDTO", new OrdersDTO());
            model.addAttribute("success", true);

        } catch (Exception ex) {
            result.addError(new FieldError("ordersDTO", "name", ex.getMessage()));
        }

        return "Orders";
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

            if(userDetails.getAuthorities().contains(roleCSA)){
                CSA csa= userRepository.findByEmail(userDetails.getUsername()).getCsa();
                System.out.println("csa:="+csa);
                ordersDTO.setCsa(csa);
                ordersDTO.setIdCSA(csa.getId());
            }else{
                CSA csa = csaRepository.getById(1); // получаем csa '---'
                ordersDTO.setCsa(csa);
                ordersDTO.setIdCSA(1);
            }
        }

        if (ordersDTO.getDevice() ==null){
            System.out.println("ordersDTO.getDevice()==null....");
            List<Device> devices = deviceRepository.findBySernum("---");
            Device device=devices.get(0);
            ordersDTO.setDevice(device);
            ordersDTO.setIdDevice(device.getId());
        }

        if (ordersDTO.getStatus() ==null){
            ordersDTO.setStatus("открыта");
        }

        if(ordersDTO.getExecutor()==null){
            System.out.println("ordersDTO.getExecutor()==null....");
            Roles role=roleRepository.findByRole("ROLE_SERV");
            System.out.println("role:= "+role);
            List<User> execs =userRepository.findByRole(role);
            System.out.println("execs:= "+execs);
            User exec = execs.get(0);
            System.out.println("exec:= "+exec);
            ordersDTO.setExecutor(exec);

//            model.addAttribute("execs", exec);
//            System.out.println("ordersDTO.getExecutor():= "+ordersDTO.getExecutor());
        }


        if(userDetails.getAuthorities().contains(roleRepository.findByType("EXECDEV"))) {
            List<User> execs = new ArrayList<>();
            execs.add(userRepository.findByEmail(userDetails.getUsername()));
            System.out.println("user: EXECDEV...");
            System.out.println("execs:= " +execs);
            model.addAttribute("execs", execs);
        }

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
        if (ordersDTO.getNum()!=null) {
            System.out.println("ordersDTO.getNum()!=null...");
            fileNames = fileService.getAllFiles(ordersDTO.getNum());
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
                if (fileService.saveFile(file,ordersDTO.getNum())){
                    fileNames.add(file.getOriginalFilename());
                }
            }
            model.addAttribute("files", fileNames);
            model.addAttribute("directory", ordersDTO.getNum());
            model.addAttribute("success",true);
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
        OrdersDTO ordersDTO =new OrdersDTO();
        ordersDTO.setId(order.getId());
        ordersDTO.setDate(order.getDate());
        ordersDTO.setNum(order.getNum());
        ordersDTO.setCsa(order.getCsa());
        ordersDTO.setIdCSA(order.getCsa().getId());
        ordersDTO.setDevice(order.getDevice());
        ordersDTO.setIdDevice(order.getDevice().getId());
        ordersDTO.setDescription(order.getDescription());
        ordersDTO.setStatus(order.getStatus());
        ordersDTO.setExecutor(order.getExecutor());
        ordersDTO.setRestore(order.getRestore());

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
            fileNames = fileService.getAllFiles(ordersDTO.getNum());
            directory=ordersDTO.getNum();
        } else {
            fileNames=new ArrayList<>();
            directory="";
        }

        List<ActTypes> actTypes = actTypesRepository.findAll();

        System.out.println("directory:="+directory);
        System.out.println("fileNames:= " +fileNames);
        System.out.println("execs:= " + execs);
        System.out.println("ordersDTO:= " +ordersDTO);

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

        List<String> fileNames=new ArrayList<>();
        fileNames = fileService.getAllFiles(ordersDTO.getNum());

        List<Roles> roles = new ArrayList<>();
        roles.add(roleRepository.findByRole("ROLE_EXECDEV"));
        roles.add(roleRepository.findByRole("ROLE_SERV"));
        List<User> execs =userRepository.findByRoleIn(roles);
        List<Restore> restoreMethods=restoreRepository.findAll();

        model.addAttribute("directory", ordersDTO.getNum());
        model.addAttribute("files", fileNames);
        model.addAttribute("restoreMethods", restoreMethods);
        model.addAttribute("execs", execs);
//        model.addAttribute("success",false);
        model.addAttribute("ordersDTO",ordersDTO);

        if (result.hasErrors()) {
            model.addAttribute("success",false);
            return "editOrder";
        }
        try {
            ordersService.save(ordersDTO);
            System.out.println("ordersService.save(ordersDTO) - выполнено успешно");

            for (MultipartFile file : files) {
               if (fileService.saveFile(file,ordersDTO.getNum())){
                   fileNames.add(file.getOriginalFilename());
               }
            }
            model.addAttribute("files", fileNames);
            model.addAttribute("success",true);
        } catch (Exception ex) {
            result.addError(new FieldError("ordersDTO", "num", ex.getMessage()));
        }
        return "/editOrder";
    }

    public List<String> getNameFilesToOrder(String subDir){
        List<String> fileNames;
        if (!subDir.isEmpty()) {
            System.out.println("getNameFilesToOrder(String subDir)...");
            System.out.println("subDir:= "+ subDir);
            fileNames = fileService.getAllFiles(subDir);
        } else {
            fileNames=new ArrayList<>();
        }
        return fileNames;
    }
}

