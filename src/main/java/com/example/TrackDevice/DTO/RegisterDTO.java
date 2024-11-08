package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @Email
    @NotEmpty
    private String email;
    @Size(min = 6, message = "Мин количетсво сим 6")
    private String password;
    private String confirmPassword;
    private Roles role;
}
