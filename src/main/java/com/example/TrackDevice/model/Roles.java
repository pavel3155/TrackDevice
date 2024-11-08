package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Roles {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private String role;
        @OneToMany (mappedBy="role",cascade = CascadeType.ALL, fetch=FetchType.EAGER)
        private List<User> users;

    }
