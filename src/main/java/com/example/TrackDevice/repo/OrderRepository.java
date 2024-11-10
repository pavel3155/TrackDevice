package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAll();
}
