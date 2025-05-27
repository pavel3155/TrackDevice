package com.example.TrackDevice.service;

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

    public OrdersDTO newOrderDTOtoObject(Order order){
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

    public List<String> loadStatusOrder() {
        List<String> orderStatus = new ArrayList<>();
        orderStatus.add("---");
        orderStatus.add("открыта");
        orderStatus.add("закрыта");
        return orderStatus;
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
