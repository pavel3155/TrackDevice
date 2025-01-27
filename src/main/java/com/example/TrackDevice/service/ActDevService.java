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

    public ActDev add(){
        ActDev actDev = new ActDev();
        actDev.setActTypes(actTypesRepository.getById((2)));
        actDev.setAct_num("02/002-Р/ТС-25");
        actDev.setAct_date(LocalDate.now());
        actDev.setFromCSA(csaRepository.getById(6));
        actDev.setToCSA(csaRepository.getById(2));
        actDev.setDevice(deviceRepository.getById(3));
        actDev.setOrder(orderRepository.getById(5));
        actDev.setNote("dddddddd");
        return actDevRepository.save(actDev);
    }


}
