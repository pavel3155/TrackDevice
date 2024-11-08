package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String date;
    private String num;
    private String description;
    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="compl_id")
    private CSA csa;
    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="dev_id")
    private Device device;

}
