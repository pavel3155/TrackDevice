package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.TypeDevice;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModelDeviceDTO {
    @NotNull
    private String name;
    @NotNull
    private TypeDevice type;
}
