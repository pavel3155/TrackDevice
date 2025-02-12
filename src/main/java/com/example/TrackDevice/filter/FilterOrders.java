package com.example.TrackDevice.filter;

import com.example.TrackDevice.model.CSA;
import lombok.Data;

@Data
public class FilterOrders {
    private String startDate;
    private String endDate;
    private String num;
    private String status;
    private CSA csa;
}
