package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.CommentDTO;
import com.example.TrackDevice.DTO.MessageDTO;
import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.*;
import com.example.TrackDevice.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrdersService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CSARepository csaRepository;
    @Autowired
    RestoreRepository restoreRepository;
    @Autowired
    FileService fileService;
    @Autowired
    ActTypesRepository actTypesRepository;


    //проверка ТС на принадлежность к КСА
    public  boolean DeviceBelongsThisCSA(long idCSA, long idCSAthisDev){
        return idCSAthisDev == idCSA;
    }
    //проверка ТС на принадлежность к КСА
    public  boolean DeviceBelongsThisCSA(OrdersDTO ordersDTO){
        return ordersDTO.getCsa().equals(ordersDTO.getDevice().getCsa());
    }
    //получаем объект Device по id
    public Device getDevice(long id){
        return deviceRepository.getById(id);
    }

    //получаем объект CSA по id
    public CSA getCSA(long id){
        return csaRepository.getById(id);
    }

    //получаем список возможных актов движения ТС
    public List<ActTypes> getActTypes(){
        List<ActTypes> actTypes = actTypesRepository.findAll();
        return actTypes;
    }

    //получаем основной каталог заявки в которой размещаются подкаталоги и файлы
    public String getFileDirectoryOrder(OrdersDTO ordersDTO){
        String directory;
        if (ordersDTO.getId()!=0) {
            directory=ordersDTO.getNum();
        } else {
            directory="";
        }
        return directory;
    }

    //получаем список имен файлов  подкаталога "pic"
    public List<String> getListFileNames(OrdersDTO ordersDTO){
        List<String> fileNames;
        if (ordersDTO.getId()!=0) {
            fileNames = fileService.getAllFiles(ordersDTO.getNum(),"pic");
        } else {
            fileNames=new ArrayList<>();
        }

        return fileNames;
    }
    //получаем список возможный методов восстановления
    public  List<Restore> getListOfRestoreMethods(){
        return restoreRepository.findAll();
    }


    //получаем список исполнителей
    public List<User> getListExecs(UserDetails userDetails){
        List<User> execs = new ArrayList<>();
        //если авторизованый пользователь с ролью "EXECDEV"-исполнитель,
        //то список "execs" будет содержать только одного этого пользователя
        if(userDetails.getAuthorities().contains(roleRepository.findByType("EXECDEV"))) {
            execs.add(userRepository.findByEmail(userDetails.getUsername()));
            System.out.println("user: EXECDEV...");
            System.out.println("execs:= " +execs);
        }
        //если авторизованый пользователь с ролью "SERV",
        //то список "execs" будет содержать пользователей с ролью "ROLE_EXECDEV"  и "ROLE_SERV"
        if(userDetails.getAuthorities().contains(roleRepository.findByType("SERV"))) {
            List<Roles> roles = new ArrayList<>();
            roles.add(roleRepository.findByRole("ROLE_EXECDEV"));
            roles.add(roleRepository.findByRole("ROLE_SERV"));
            execs = userRepository.findByRoleIn(roles);
            System.out.println("execs:= " +execs);
        }
        //если авторизованый пользователь с ролью "CSA",
        //то список "execs" будет содержать пользователей с ролью "ROLE_SERV"
        if(userDetails.getAuthorities().contains(roleRepository.findByType("CSA"))) {
            execs = userRepository.findByRole(roleRepository.findByRole("ROLE_SERV"));
            System.out.println("execs:= " +execs);
        }
        return execs;
    }
    public CSA getCSAfromUserDetails(UserDetails userDetails){
        //если пользователь с ролью CSA
        if(userDetails.getAuthorities().contains(roleRepository.findByType("CSA")))        {
            CSA csa= userRepository.findByEmail(userDetails.getUsername()).getCsa();//получаем csa(объект) из пользователя
            return csa;
        }else{
            CSA csa = csaRepository.getByNum("---"); // получаем csa '---'
            return csa;
            //ordersDTO.setIdCSA(csa.getId());
        }
    }
    public String getNumCSAfromUserDetails(UserDetails userDetails){
        return userRepository.findByEmail(userDetails.getUsername()).getCsa().getNum();
    }


    public List<MessageDTO> addMessage(UserDetails userDetails,MessageDTO messageDTO){

        String numCsa =getNumCSAfromUserDetails(userDetails);
        List<MessageDTO> messages;
        if(!messageDTO.getMessage().isEmpty()) {
            int i=userDetails.getUsername().indexOf('@');
            String user = userDetails.getUsername().substring(0, i);
            String coment = numCsa + ":" + user + ":" + messageDTO.getMessage();
            String subDir = messageDTO.getNum();
            String fileName = "consult_" + messageDTO.getNum() + ".txt";
            String pathFile = fileService.createSubDirCons(subDir, fileName);
            fileService.addComment(pathFile, coment);
            messages = getListMessages(subDir);
        } else messages=new ArrayList<>();
        return messages;
    }

    public Device getDeviceDefault(){
        List<Device> devices = deviceRepository.findBySernum("---");
        Device device=devices.get(0);
        return device;
    }

    public  User getExecutorDefault(){
        List<User> execs =userRepository.findByRole(roleRepository.findByRole("ROLE_SERV"));//получаем всех пользователей с ролью "ROLE_SERV"
        System.out.println("execs:= "+execs);
        User exec = execs.get(0);//получаем первого пользователя из списка
        System.out.println("exec:= "+exec);
        return exec;
    }


    public List<Order> getListOfUsersOrders(Object role, UserDetails userDetails){
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
        return orders;
    }

    public Object getRoleFromUserDetails(UserDetails userDetails){
        Object[] arrRoles=userDetails.getAuthorities().stream().toArray();
        System.out.println("arrRoles[0]= "+arrRoles[0]);
        Object role=arrRoles[0].toString();
        System.out.println("arrRoles[0].toString()="+role);
        return role;
    }

    public List<MessageDTO> getListMessages(String numOrder){
        return fileService.getListMessages(numOrder);
    }


    public Boolean btnCSADisplay(OrdersDTO orderDTO){

        if (orderDTO.getStatus().equals("закрыта")){
           return false;
        }
        if (!DeviceBelongsThisCSA(orderDTO)){
            return false;
        }
        return true;
    }
    public Boolean btnSelDeviceDisplay(OrdersDTO orderDTO){
        if (orderDTO.getStatus().equals("закрыта")){
            return false;
        }
        return true;
    }
    public Boolean btnSelDeviceDisplay(CSA oCSA, Device oDevice){
        if (!oCSA.getNum().equals("---")&&!oDevice.getSernum().equals("---")){
            Device device = deviceRepository.getById(oDevice.getId());
            CSA csaDevice = device.getCsa();
            System.out.println("oCSA="+oCSA);
            System.out.println("csaDevice="+csaDevice);
            if (oCSA==csaDevice){
                System.out.println("..oCSA==csaDevice..");
                return true;
            } else {
                return false;
            }
        }else {
            return true;
        }
    }
    public LocalDate toLocalData(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        System.out.println("localDate ="+localDate);
        return localDate;
    }
    public Order add(OrdersDTO ordersDTO){
      Order order = new Order();
      order.setDate(toLocalData(ordersDTO.getDate()));
      order.setNum(ordersDTO.getNum());
      order.setCsa(ordersDTO.getCsa());
      order.setDevice(ordersDTO.getDevice());
      order.setDescription(ordersDTO.getDescription());
      order.setStatus(ordersDTO.getStatus());
      order.setExecutor(ordersDTO.getExecutor());
      order.setRestore(ordersDTO.getRestore());
      order.setServiceable(ordersDTO.getServiceable());

      return orderRepository.save(order);
    }
    public Order save(OrdersDTO ordersDTO){
        Order order = new Order();
        order.setId(ordersDTO.getId());
        order.setDate(toLocalData(ordersDTO.getDate()));
        order.setNum(ordersDTO.getNum());
        order.setCsa(ordersDTO.getCsa());
        order.setDevice(ordersDTO.getDevice());
        order.setDescription(ordersDTO.getDescription());
        order.setStatus(ordersDTO.getStatus());
        order.setExecutor(ordersDTO.getExecutor());
        order.setRestore(ordersDTO.getRestore());

        if(ordersDTO.getServiceable()!=null){
            order.setServiceable(ordersDTO.getServiceable());
        }
        if (ordersDTO.getDateClosingOrder()!=null&&!(ordersDTO.getDateClosingOrder().isEmpty())){
            order.setDate_closing(toLocalData(ordersDTO.getDateClosingOrder()));
        }
        return orderRepository.save(order);
    }
    public OrdersDTO objOrderDTOCreate(long orderID,UserDetails userDetails){
        OrdersDTO orderDTO;
        if(orderID==0){
            orderDTO= new OrdersDTO();
            //устанавливаем свойство csa объекта ordersDTO, в соответствии с csa объекта userDetails
            orderDTO.setCsa(getCSAfromUserDetails(userDetails));
            //устанавливаем свойство idCsa объекта ordersDTO, в соответствии с id объекта userDetails
            orderDTO.setIdCSA(getCSAfromUserDetails(userDetails).getId());
            //устанавливаем значение device "---"
            orderDTO.setDevice(getDeviceDefault());
            //устанавливаем значение id объекта device "---"
            orderDTO.setIdDevice(getDeviceDefault().getId());
            //устанавливаем статус заявки
            orderDTO.setStatus("открыта");
            //устанавливаем сойство Executor в соответствии с первым пользователем из списка пользователей с ролью "ROLE_SERV"
            orderDTO.setExecutor(getExecutorDefault());
            //устанавливаем сойство restore(способ восстановление)
            orderDTO.setRestore(restoreRepository.getByMethod("---"));
        } else {
            orderDTO = getOrderDTOfromOrder(orderRepository.getById(orderID));
        }
        return orderDTO;
    }
    public OrdersDTO getOrderDTOfromOrder(Order order){
        OrdersDTO ordersDTO =new OrdersDTO();
        ordersDTO.setId(order.getId());
        ordersDTO.setDate(order.getDate().toString());
        ordersDTO.setNum(order.getNum());
        ordersDTO.setCsa(order.getCsa());
        ordersDTO.setIdCSA(order.getCsa().getId());
        ordersDTO.setDevice(order.getDevice());
        ordersDTO.setIdDevice(order.getDevice().getId());
        ordersDTO.setDescription(order.getDescription());
        ordersDTO.setStatus(order.getStatus());
        ordersDTO.setExecutor(order.getExecutor());
        ordersDTO.setRestore(order.getRestore());
        ordersDTO.setServiceable(order.getServiceable());
        if (order.getDate_closing()!=null){
            ordersDTO.setDateClosingOrder(order.getDate_closing().toString());
        }
        return ordersDTO;
    }

    //получаем список с возможным статусом (состоянием) заявки
    public List<String> loadStatusOrder() {
        List<String> orderStatus = new ArrayList<>();
        orderStatus.add("---");
        orderStatus.add("открыта");
        orderStatus.add("закрыта");
        return orderStatus;
    }
    public String numOrder(String OrderDate){
        String orderNum;
        List<Order> orders = orderRepository.findAllByNumStartingWith(OrderDate);
        List<Long> lstNum =orders.stream()
                .map(o -> {
                    try {
                        // Извлекаем подстроку и преобразуем в целое число
                        return Long.parseLong(o.getNum());
                    } catch (NumberFormatException e) {
                        // Обработка ошибок: если строка не может быть преобразована или индекс вне диапазона
                        System.err.println("Ошибка при обработке номера: " + o.getNum());
                        return null; // Возвращаем null, чтобы отфильтровать позже
                    }
                })
                .filter(num -> num != null) // Удаляем null значения
                .toList(); // Собираем результаты в список
        // Выводим список целых чисел
        lstNum.forEach(System.out::println);
        //List<String> lstNum =orders.stream().map(o->new String(o.getNum())).collect(Collectors.toList());

        long maxNum = Collections.max(lstNum) + 1;
        System.out.println("maxNum=" + maxNum);

        orderNum= String.valueOf(maxNum);


        return orderNum;

    }

    public String GenerationNumOrder(List<Order> orders){
        String num;
        if(!orders.isEmpty()) {
            List<Integer> lstNum = new ArrayList<>();
            for (Order order : orders) {
                lstNum.add(Integer.parseInt(order.getNum().substring(8)));
            }

            System.out.println("lstNum=" + lstNum);
            System.out.println("max=" + Collections.max(lstNum));

            int intNUM = Collections.max(lstNum) + 1;
            System.out.println("intNUM=" + intNUM);


            num = String.valueOf(intNUM);
            System.out.println("num=" + num);
            int l = num.length();
            System.out.println("l=" + l);

            switch (l) {
                case 1:
                    num = "000" + num;
                    break;
                case 2:
                    num = "00" + num;
                    break;
                case 3:
                    num = "0" + num;
                    break;
                default:
                    break;
            }
        } else {
            num = "0001";
        }

        return num;

    }














}
