package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.TypeDevice;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeviceDTO {
    //private TypeDevice type;
    private long id;
    @NotNull
    private ModelDevice model;
    private String inv_num;
    @NotNull
    private String ser_num;
    private String status;

}
