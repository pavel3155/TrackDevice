package com.example.TrackDevice.DTO;

import lombok.Data;

@Data
public class MessageDTO {
    private Long idOrder;
    private String num;
    private String csa;
    private String user;
    private String message;
}
