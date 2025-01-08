package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.Restore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestoreRepository extends JpaRepository<Restore,Long> {
    List<Restore> findAll();
    Restore getByMethod(String method);


}
