package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.repo.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    public Device addDevice(DeviceDTO deviceDTO){
        Device device = new Device();
        device.setModel(deviceDTO.getModel());
        device.setSer_num(device.getSer_num());
        device.setInv_num(device.getInv_num());
        device.setStatus(device.getStatus());
        return deviceRepository.save(device);
    }
}
