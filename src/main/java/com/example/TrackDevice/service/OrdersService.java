package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.ActTypes;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.Order;
import com.example.TrackDevice.repo.DeviceRepository;
import com.example.TrackDevice.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Boolean btnSelDeviceDisplay(CSA oCSA, Device oDevice){
        if (!oCSA.getNum().equals("---")&&!oDevice.getSernum().equals("---")){
            Device device = deviceRepository.getById(oDevice.getId());
            CSA csaDevice = device.getCsa();
            if (oCSA==csaDevice){
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
