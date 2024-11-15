package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.TypeDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelDeviceRepository extends JpaRepository<ModelDevice,Long> {
    List<ModelDevice> findByType(TypeDevice type);
}
