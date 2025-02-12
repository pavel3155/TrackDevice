package com.example.TrackDevice.specification;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class OrdersSpecification {
    public Specification<Order> dateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("date"), startDate, endDate);
    }
    public Specification<Order> hasNum(String num) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("num"), "%" + num + "%");
    }
    public Specification<Order> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }
    public Specification<Order> hasCSA(CSA csa) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("csa"), csa);
    }


}
