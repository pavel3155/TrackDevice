package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Compl;
import com.example.TrackDevice.model.Device;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdersDTO {
    @NotEmpty
    private String date;
    @NotEmpty
    private String num;
    @NotEmpty
    private String description;
    @NotNull
    private CSA csa;
    @NotNull
    private Device device;
}
