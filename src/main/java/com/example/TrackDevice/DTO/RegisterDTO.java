package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Roles;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @Email
    @NotEmpty
    @NotBlank
    @NotNull
    private String email;
    @Size(min = 6, message = "Мин количетсво сим 6")
    @NotNull
    private String password;
    private String confirmPassword;
    @NotNull
    private Roles role;
    @NotNull
    private CSA csa;
}
