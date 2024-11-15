package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "model_device")
public class ModelDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="type_id")
    private TypeDevice type;

}
