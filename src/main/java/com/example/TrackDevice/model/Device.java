package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    private String model;
    private String inv_num;
    private String ser_num;
//    @OneToMany (mappedBy="device",cascade = CascadeType.ALL, fetch=FetchType.EAGER)
//    private List<Order> orders;
}
