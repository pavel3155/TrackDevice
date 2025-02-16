package com.example.TrackDevice.DTO;

import com.example.TrackDevice.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class ConsultDTO {
    String num;
    Order order;
    String newComment;
    List<String> comments;
}
