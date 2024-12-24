package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.DTO.ModelDeviceDTO;
import com.example.TrackDevice.DTO.TypeDeviceDTO;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.TypeDevice;
import com.example.TrackDevice.repo.DeviceRepository;
import com.example.TrackDevice.repo.ModelDeviceRepository;
import com.example.TrackDevice.repo.TypeDeviceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    ModelDeviceRepository modelDeviceRepository;
    @Autowired
    TypeDeviceRepository typeDeviceRepository;

    public Device addDevice(@Valid DeviceDTO deviceDTO){
        Device device = new Device();
        device.setModel(deviceDTO.getModelDevice());
        device.setSernum(deviceDTO.getSernum());
        device.setInvnum(deviceDTO.getInvnum());
        device.setCsa(deviceDTO.getCsa());
        //newDevice.setStatus(deviceDTO.getStatus());
        return deviceRepository.save(device);
    }
    public Device saveDevice(@Valid DeviceDTO deviceDTO){
        Device device = new Device();
        device.setId(deviceDTO.getId());
        device.setModel(deviceDTO.getModelDevice());
        device.setSernum(deviceDTO.getSernum());
        device.setInvnum(deviceDTO.getInvnum());
        device.setCsa(deviceDTO.getCsa());
        //newDevice.setStatus(deviceDTO.getStatus());
        return deviceRepository.save(device);
    }
    public TypeDevice saveTypeDevice(TypeDeviceDTO typeDeviceDTO){
        TypeDevice typeDevice = new TypeDevice();
        typeDevice.setId(typeDeviceDTO.getId());
        typeDevice.setType(typeDeviceDTO.getType());
        return typeDeviceRepository.save(typeDevice);
    }
    public TypeDevice addTypeDevice(TypeDeviceDTO typeDeviceDTO){
        TypeDevice typeDevice = new TypeDevice();
        typeDevice.setType(typeDeviceDTO.getType());
        return typeDeviceRepository.save(typeDevice);
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
