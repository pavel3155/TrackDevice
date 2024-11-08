package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
    List<Orders> findAll();
}
