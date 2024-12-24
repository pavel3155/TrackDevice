package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "devicecsa")
public class DeviceCSA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "")
    private  long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name="csa_id")
    private CSA csa;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn (name="dev_id")
    private Device device;

}
