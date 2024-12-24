package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.DeviceCSA;
import com.example.TrackDevice.model.ModelDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceCSARepository extends JpaRepository<DeviceCSA,Long> {
    List<Device> findByCsa(CSA csa);
}
