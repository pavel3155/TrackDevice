package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "act_dev")
public class ActDev {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String num;
    private LocalDate date;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="type_id", nullable = false)
    private ActTypes types;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="csa_from_id", nullable = false)
    private CSA fromCSA;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="csa_to_id", nullable = false)
    private CSA toCSA;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="dev_id")
    private Device device;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="order_id")
    private Order order;
    private String note;



}
