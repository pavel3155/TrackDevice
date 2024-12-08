package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.CSA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CSARepository extends JpaRepository<CSA,Long>{
    List<CSA> findAll();
    CSA getById(long id);
    @Query("SELECT DISTINCT code FROM CSA")
    List<String> findDistinctCode();
    List<CSA> findByCode(String code);

}
