package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    List<Device> findAll();
}
