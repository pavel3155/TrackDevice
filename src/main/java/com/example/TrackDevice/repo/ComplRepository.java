package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.Compl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplRepository extends JpaRepository<Compl,Long> {
    List<Compl> findAll();
}
