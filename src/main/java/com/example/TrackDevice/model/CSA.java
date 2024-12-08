package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
