package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.TypeDevice;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeDeviceRepository extends JpaRepository<TypeDevice,Long> {
    List<TypeDevice> findAll();
    TypeDevice findByType(String type);

}
