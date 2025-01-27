package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.ActDev;
import com.example.TrackDevice.model.CSA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActDevRepository extends JpaRepository<ActDev,Long> {
    List<ActDev> findAll();
    ActDev getById(long id);
}
