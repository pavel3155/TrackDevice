package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.DTO.ModelDeviceDTO;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.repo.DeviceRepository;
import com.example.TrackDevice.repo.ModelDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    ModelDeviceRepository modelDeviceRepository;

    public Device addDevice(DeviceDTO deviceDTO){
        Device device = new Device();
        device.setModel(deviceDTO.getModel());
        device.setSer_num(device.getSer_num());
        device.setInv_num(device.getInv_num());
        device.setStatus(device.getStatus());
        return deviceRepository.save(device);
    }

    public ModelDevice saveModelDevice(ModelDeviceDTO modelDeviceDTO){
        ModelDevice modelDevice =new ModelDevice();
        modelDevice.setId(modelDeviceDTO.getId());
        modelDevice.setName(modelDeviceDTO.getName());
        modelDevice.setType(modelDeviceDTO.getType());
        return modelDeviceRepository.save(modelDevice);
    }

    public ModelDevice addModelDevice(ModelDeviceDTO modelDeviceDTO){
        ModelDevice modelDevice =new ModelDevice();
        modelDevice.setName(modelDeviceDTO.getName());
        modelDevice.setType(modelDeviceDTO.getType());
        return modelDeviceRepository.save(modelDevice);
    }

}
