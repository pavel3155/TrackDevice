package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.ActDev;
import com.example.TrackDevice.model.ActTypes;
import com.example.TrackDevice.model.Order;
import com.example.TrackDevice.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ActDevService {
    @Autowired
    ActDevRepository actDevRepository;
    @Autowired
    ActTypesRepository actTypesRepository;
    @Autowired
    CSARepository csaRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    OrderRepository orderRepository;

    public ActDev add(String num, LocalDate localDate){
        ActDev actDev = new ActDev();
        actDev.setTypes(actTypesRepository.getById((2)));
        actDev.setNum(num);
        actDev.setDate(localDate);
        actDev.setFromCSA(csaRepository.getById(9));
        actDev.setToCSA(csaRepository.getById(5));
        actDev.setDevice(deviceRepository.getById(63));
        actDev.setOrder(orderRepository.getById(11));
        actDev.setNote("dddddddd");
        return actDevRepository.save(actDev);
    }


}
