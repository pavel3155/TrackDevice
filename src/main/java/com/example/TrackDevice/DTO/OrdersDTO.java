package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Compl;
import com.example.TrackDevice.model.Device;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdersDTO {
    @NotNull
    private String date;
    @NotNull
    private String num;
    private String description;
    private CSA csa;
    private Device device;
}
