package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.ActDevDTO;
import com.example.TrackDevice.DTO.OrdersDTO;
import com.example.TrackDevice.model.ActDev;
import com.example.TrackDevice.model.ActTypes;
import com.example.TrackDevice.model.Order;
import com.example.TrackDevice.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public LocalDate toLocalData(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        System.out.println("localDate ="+localDate);
        return localDate;
    }
//    public Date toData(String strDate){
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = formatter.parse(strDate,1);
//
//
//
//        System.out.println("localDate ="+localDate);
//        return Date;
//    }
    public ActDev add(ActDevDTO actDevDTO){
        ActDev actDev = new ActDev();
        actDev.setTypes(actDevDTO.getActType());
        actDev.setNum(actDevDTO.getNum());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(actDevDTO.getDate(), formatter);
        actDev.setDate(localDate);

        actDev.setFromCSA(actDevDTO.getFromCSA());
        actDev.setToCSA(actDevDTO.getToCSA());
        actDev.setDevice(actDevDTO.getDevice());
        actDev.setOrder(actDevDTO.getOrder());
        actDev.setNote(actDevDTO.getNote());
        return actDevRepository.save(actDev);
    }
    @Transactional
    public void del(ActDev actDev){
        actDevRepository.delete(actDev);
    }

}
