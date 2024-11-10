package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.CSA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CSARepository extends JpaRepository<CSA,Long>{
    List<CSA> findAll();
}
