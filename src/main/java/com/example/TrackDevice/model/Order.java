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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String date;
    @Column(unique = true,nullable = false)
    private String num;
    private String description;
    //@ManyToOne (optional=false, cascade=CascadeType.ALL)
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="compl_id")
    private CSA csa;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="dev_id")
    private Device device;
    private String status;

}
