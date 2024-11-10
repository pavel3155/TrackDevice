package com.example.TrackDevice.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ComplDTO {
    @NotEmpty
    private String num;
    private String address;

}
