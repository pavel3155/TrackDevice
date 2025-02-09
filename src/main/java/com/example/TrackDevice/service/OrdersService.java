package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.Order;
import com.example.TrackDevice.repo.DeviceRepository;
import com.example.TrackDevice.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
}
