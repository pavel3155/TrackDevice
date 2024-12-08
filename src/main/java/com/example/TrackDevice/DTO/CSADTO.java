package com.example.TrackDevice.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CSADTO {
    private long id;
    @NotNull
    private String num;
    @NotNull
    private String code;
    private String address;
}
