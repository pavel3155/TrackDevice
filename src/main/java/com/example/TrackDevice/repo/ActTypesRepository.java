package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.ActTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActTypesRepository extends JpaRepository<ActTypes,Long> {
    ActTypes getById(long id);
    List<ActTypes> findAll();

}
