package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.ActTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActTypesRepository extends JpaRepository<ActTypes,Long> {
    ActTypes getById(long id);
}
