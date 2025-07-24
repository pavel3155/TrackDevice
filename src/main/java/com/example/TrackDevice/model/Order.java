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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    240725
//    private String odate;
    private LocalDate date;
    private LocalDate date_closing;
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
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="user_id", nullable = false)
    private User executor;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="restore_id", nullable = false)
    private Restore restore;
    private Boolean serviceable;




}
