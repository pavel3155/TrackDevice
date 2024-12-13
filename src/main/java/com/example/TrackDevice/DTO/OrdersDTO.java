package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdersDTO {
    private long id;
    @NotNull
    private String date;
    private String num;
    private String description;
    private CSA csa;
    private long idCSA;
    private Device device;
    private long idDevice;
    private String status;
}
