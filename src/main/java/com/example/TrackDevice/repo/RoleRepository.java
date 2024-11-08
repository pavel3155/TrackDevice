package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleRepository extends JpaRepository<Roles,Long>{
    Roles findByRole(String role);
    List<Roles> findAll();
}
