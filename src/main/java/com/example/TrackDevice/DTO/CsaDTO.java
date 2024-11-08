package com.example.TrackDevice.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
@Data
public class CsaDTO {
    @NotNull
    private String num;
    private String address;

}
