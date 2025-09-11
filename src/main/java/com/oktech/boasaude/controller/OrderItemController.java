package com.oktech.boasaude.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;


import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.oktech.boasaude.dto.OrderItemResponseDto;

import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.service.OrderItemService;


/**
 * Controller for managing orders in the application.
 * This controller handles HTTP requests related to orders, such as creating,
 * updating, and retrieving orders.
 * 
 * @author João Martins
 * @version 1.0
 */


@RestController
@RequestMapping("/v1/orders-items")

public class OrderItemController {

    private final OrderItemService orderItemService;

    private static final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/sold")
    public ResponseEntity<List<OrderItemResponseDto>> getSoldItems(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("User not authenticated");
            return ResponseEntity.status(401).build();
        }

        User currentUser = (User) authentication.getPrincipal();
        List<OrderItemResponseDto> soldItems = orderItemService.getSoldItems(currentUser);
        logger.info("Sold items retrieved successfully for user ID: {}", currentUser.getId());
        return ResponseEntity.ok(soldItems);
    }

    @PostMapping("/delivery/{orderItemId}/")
    public ResponseEntity<OrderItemResponseDto> updateOrderItemDeliveryStatus(
        @PathVariable UUID orderItemId, 
        @RequestParam String status, 
        Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        OrderItemResponseDto updatedItem = orderItemService.updateDeliveryStatus(orderItemId, currentUser, status);
        return ResponseEntity.ok(updatedItem);
    }
}
