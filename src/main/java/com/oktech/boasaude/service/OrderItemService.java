package com.oktech.boasaude.service;

import java.util.List;
import java.util.UUID;

import com.oktech.boasaude.dto.OrderItemResponseDto;
import com.oktech.boasaude.entity.Order;
import com.oktech.boasaude.entity.OrderItem;
import com.oktech.boasaude.entity.User;

/**
 * Interface for managing order items in the application.
 * This service provides methods to handle order items, such as retrieving,
 * creating, updating, and deleting order items associated with an order.
 * 
 * @author João Martins
 * @version 1.0
 */


public interface OrderItemService {

    OrderItem addOrderItem(Order order, UUID productId, int quantity, UUID addressId, User currentUser);

    List<OrderItem> getOrderItemsByOrderId(UUID orderId);

    List<OrderItemResponseDto> getSoldItems(User user);

    OrderItemResponseDto updateDeliveryStatus(UUID orderItemId, User currentUser, String status);
}
