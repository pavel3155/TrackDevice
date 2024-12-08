package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.CSADTO;
import com.example.TrackDevice.DTO.DeviceDTO;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.repo.CSARepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CSAService {
    @Autowired
    CSARepository csaRepository;

    public CSA saveCSA(@Valid CSADTO csaDTO){
        CSA csa = new CSA();
        csa.setId(csaDTO.getId());
        csa.setCode(csaDTO.getCode());
        csa.setNum(csaDTO.getNum());
        csa.setAddress(csaDTO.getAddress());
        return csaRepository.save(csa);
    }

    public CSA addCSA(@Valid CSADTO csaDTO){
        CSA csa = new CSA();
        csa.setCode(csaDTO.getCode());
        csa.setNum(csaDTO.getNum());
        csa.setAddress(csaDTO.getAddress());
        return csaRepository.save(csa);
    }
}
