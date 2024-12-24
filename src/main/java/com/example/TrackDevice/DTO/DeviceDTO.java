package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.TypeDevice;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceDTO {
    //private TypeDevice type;
    private long id;
    private ModelDevice modelDevice;
    private String invnum;
    @NotNull
    private String sernum;
    private String status;
    private CSA csa;
}
