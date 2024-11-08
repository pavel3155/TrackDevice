package com.example.TrackDevice.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeviceDTO {
    private String type;
    private String model;
    private String inv_num;
    @NotNull
    private String ser_num;
}
