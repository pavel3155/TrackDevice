package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.ModelDevice;
import com.example.TrackDevice.model.TypeDevice;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class TypeDeviceDTO {
    private long id;
    @NotNull
    private String type;
}
