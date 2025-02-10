package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrdersDTO {
    private long id;
    @NotNull
    private String date;
    private String dateClosingOrder;
    @NotEmpty
    @Size(min = 11, message = "Мин количетсво сим 11")
    private String num;
    private String description;
    private CSA csa;
    private long idCSA;
    private Device device;
    private long idDevice;
    private String status;
    private User executor;
    private Restore restore;
    private ActTypes actTypes;
    private Boolean serviceable;
    private String err;
}
