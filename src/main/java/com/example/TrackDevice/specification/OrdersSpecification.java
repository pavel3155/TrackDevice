package com.example.TrackDevice.specification;

import com.example.TrackDevice.model.CSA;
import com.example.TrackDevice.model.Order;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

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
    public static Specification<Order> hasDateDifferenceGreaterThan7Days() {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // Получаем startDate и endDate
            Expression<Date> startDate = root.get("date");
            Expression<Date> endDate = root.get("date_closing");

            // Если endDate равно null, используем текущую дату
            Expression<Date> effectiveEndDate = cb.coalesce(endDate, cb.currentDate());

            // Вычисляем разницу в днях
            //Expression<Long> diffInDays = cb.quot(cb.diff(cb.function("EXTRACT", Number.class, cb.literal("EPOCH"), effectiveEndDate), cb.function("EXTRACT", Number.class, cb.literal("EPOCH"), startDate)),86400 );
            Expression<Long> diffInDays = cb.function("EXTRACT", Long.class,
                    cb.literal("DAY"), cb.diff(effectiveEndDate, startDate));
            // Создаем предикат для фильтрации (разница в днях > 7)
            return cb.gt(diffInDays, 7);
        };
    }

}
