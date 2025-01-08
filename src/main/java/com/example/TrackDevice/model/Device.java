package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="model_id")
    private ModelDevice model;
    private String invnum;
    private String sernum;
    private String status;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="csa_id", nullable = false)
    private CSA csa;



}
