package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "csa")
public class CSA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "")
    private long id;
    private String num;
    private String code;
    private String address;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="dev_id")
    private Device device;

//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn (name="dev_id")
//    private List<Device> devices = new ArrayList<>();

}
