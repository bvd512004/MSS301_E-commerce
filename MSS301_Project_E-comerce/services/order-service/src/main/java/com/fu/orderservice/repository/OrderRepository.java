package com.fu.orderservice.repository;

import com.fu.orderservice.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

    @Query("""
            SELECT DISTINCT o FROM CustomerOrder o
            LEFT JOIN FETCH o.items
            ORDER BY o.createdAt DESC
            """)
    List<CustomerOrder> findAllWithItems();

    @Query("""
            SELECT DISTINCT o FROM CustomerOrder o
            LEFT JOIN FETCH o.items
            WHERE o.userId = :userId
            ORDER BY o.createdAt DESC
            """)
    List<CustomerOrder> findByUserIdWithItems(@Param("userId") Long userId);

    @Query("""
            SELECT o FROM CustomerOrder o
            LEFT JOIN FETCH o.items
            WHERE o.id = :id
            """)
    Optional<CustomerOrder> findByIdWithItems(@Param("id") Long id);

}
