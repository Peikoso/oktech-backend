package com.oktech.boasaude.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oktech.boasaude.entity.OrderItem;
import com.oktech.boasaude.entity.OrderStatus;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findByOrderId(UUID orderId);
    
    List<OrderItem> findByOrder_StatusAndProduct_Shop_IdOrderByCreatedAtDesc(OrderStatus status, UUID shopId);

    Optional<OrderItem> findByIdAndProduct_Shop_Owner_Id(UUID orderItemId, UUID userId);
}
