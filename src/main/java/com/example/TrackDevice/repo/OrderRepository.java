package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Order;
import com.example.TrackDevice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAll();
    Order getById(long id);
    List<Order> getByCsa(CSA csa);
    List<Order> getByExecutor(User executor);
}
