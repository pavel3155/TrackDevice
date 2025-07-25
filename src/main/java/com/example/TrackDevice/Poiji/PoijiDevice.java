package com.example.TrackDevice.Poiji;

import com.poiji.annotation.ExcelCell;
import lombok.Data;

@Data
public class PoijiDevice {
    @ExcelCell(1)
    private String type;

    @ExcelCell(2)
    private String model;

    @ExcelCell(3)
    private String invNum;

    @ExcelCell(4)
    private String serNum;




}
