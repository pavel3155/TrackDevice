package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.Roles;
import com.example.TrackDevice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    List<User> findByRole(Roles role);
    List<User> findByRoleIn(List<Roles> roles);
    List<User> findAll();
    User getById(Long id);

}
