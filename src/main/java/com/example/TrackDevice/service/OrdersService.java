package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.Order;
import com.example.TrackDevice.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {
    @Autowired
    OrderRepository orderRepository;
    public Order add(OrdersDTO ordersDTO){
      Order order = new Order();
      order.setDate(ordersDTO.getDate());
      order.setNum(ordersDTO.getNum());
      order.setCsa(ordersDTO.getCsa());
      order.setDevice(ordersDTO.getDevice());
      order.setDescription(ordersDTO.getDescription());
      order.setStatus(ordersDTO.getStatus());
      order.setExecutor(ordersDTO.getExecutor());
      order.setRestore(ordersDTO.getRestore());
      return orderRepository.save(order);
    }
    public Order save(OrdersDTO ordersDTO){
        Order order = new Order();
        order.setId(ordersDTO.getId());
        order.setDate(ordersDTO.getDate());
        order.setNum(ordersDTO.getNum());
        order.setCsa(ordersDTO.getCsa());
        order.setDevice(ordersDTO.getDevice());
        order.setDescription(ordersDTO.getDescription());
        order.setStatus(ordersDTO.getStatus());
        order.setExecutor(ordersDTO.getExecutor());
        order.setRestore(ordersDTO.getRestore());
        return orderRepository.save(order);
    }
}
