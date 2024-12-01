package com.example.TrackDevice.DTO;

import lombok.Data;

@Data
public class POSTDeviceDTO {
    private long idModel;//id ModelDevice
    private  String type;
    private String modelDevice;
    private String inv_num;
    private String ser_num;
}
