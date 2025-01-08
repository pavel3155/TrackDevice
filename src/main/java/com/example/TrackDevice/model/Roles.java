package com.example.TrackDevice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Roles implements GrantedAuthority  {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private String type;
        private String role;


        @Override
        public String getAuthority() {
                return getRole();
        }
}
