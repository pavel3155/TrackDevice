package com.example.TrackDevice.DTO;
import com.example.TrackDevice.model.ActTypes;
import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Device;
import com.example.TrackDevice.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActDevDTO {
    private long id;
    @NotNull
    private String num;
    private String dateEND;
    @NotNull
    private String date;
    @NotNull
    private ActTypes actType;
    @NotNull
    private CSA fromCSA;
    @NotNull
    private CSA toCSA;
    @NotNull
    private Device device;
    @NotNull
    private Order order;
    @NotNull
    private String note;
    private long idSelDev;
    private  long idFromCSA;
    private String err;

}
