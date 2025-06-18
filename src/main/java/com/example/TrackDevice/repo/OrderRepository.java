package com.example.TrackDevice.repo;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Order;
import com.example.TrackDevice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByOrderByDateAsc();
    List<Order> findAll();
    Order getById(long id);
    List<Order> getByCsa(CSA csa);
    List<Order> getByExecutor(User executor);
    List<Order> findAllByNumStartingWith(String date);


//    @Query("select * from Orders o where  COALESCE(o.date_closing, CURRENT_DATE)>o.date")

    //@Query("SELECT id FROM Order o WHERE EXTRACT(DAY FROM AGE(COALESCE(o.date_closing, CURRENT_DATE), o.date)) > 5")
    @Query(value = "SELECT id FROM orders o WHERE (COALESCE(o.date_closing, CURRENT_DATE) - o.date) > 5", nativeQuery = true)
    List<Long> idOrdersDiffDaysGT5();
    List<Order> findByIdIn(List<Long> lstId);

//    @Query(value = "SELECT * FROM orders o WHERE (COALESCE(o.date_closing, CURRENT_DATE) - o.date) > 5", nativeQuery = true)
//    List<Order> diffDaysGT5();

}
