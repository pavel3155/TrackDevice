package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.ModelDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    List<Device> findAll();
    List<Device> findByModel(ModelDevice modelDevice);

}
