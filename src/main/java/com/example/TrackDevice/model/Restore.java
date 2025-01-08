package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restore")
public class Restore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "")
    private long id;
    private String method;
}
