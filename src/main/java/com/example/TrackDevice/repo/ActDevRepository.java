package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.ActDev;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActDevRepository extends JpaRepository<ActDev,Long> {
    List<ActDev> findAll();
    List<ActDev> findByOrder(Order order);
    ActDev getById(long id);
    List<ActDev> findByNumAndOrderNot(String num, Order order);
    List<ActDev> findByNumAndOrderAndFromCSAAndDevice(String num, Order order, CSA fromCSA, Device device);
    List<ActDev> findByNumAndOrderAndToCSAAndDevice(String num, Order order, CSA ToCSA, Device device);
    List<ActDev> findAllByDateBetween(LocalDate start, LocalDate finish);
    List<ActDev> findAllByDateAfter(LocalDate start);
    List<ActDev> findAllByDateBetweenAndNumContainingIgnoreCase(LocalDate start, LocalDate finish,String num);
    List<ActDev> findAllByNumContainingIgnoreCase(String num);
}
