package com.oktech.boasaude.dto;

import java.math.BigInteger;
import java.util.UUID;

import com.oktech.boasaude.entity.OrderItem;

public record OrderItemResponseDto(
    UUID id,
    UUID productId,
    String productName,
    int quantity,
    BigInteger totalPrice,
    String deliveryStatus,
    String addressId
) {
    public OrderItemResponseDto(OrderItem item) {
        this(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getQuantity(),
            item.getTotalPrice(),
            item.getDeliveryStatus().getStatus(),
            item.getAddress().getId().toString()
        );
    }
}
