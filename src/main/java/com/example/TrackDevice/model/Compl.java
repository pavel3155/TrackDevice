package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compl")
public class Compl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "")
    private long id;
    private String num;
    private String address;
//    @OneToMany (mappedBy="csa",cascade = CascadeType.ALL, fetch=FetchType.EAGER)
//    private List<Order> orders;

}
