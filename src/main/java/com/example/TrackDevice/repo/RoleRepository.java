package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles,Long>{
    Roles findByType(String type);
    Roles getById(long id);
    List<Roles> findAll();
}
