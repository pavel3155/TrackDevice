package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrdersDTO {
    @NotNull
    private String date;
    @NotNull
    private String num;
    @NotNull
    private String description;
    @NotNull
    private CSA csa;
    @NotNull
    private Device device;

}
