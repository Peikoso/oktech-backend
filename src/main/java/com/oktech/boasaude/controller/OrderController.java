package com.oktech.boasaude.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import com.oktech.boasaude.dto.CreateOrderItemDto;
import com.oktech.boasaude.dto.OrderItemResponseDto;
import com.oktech.boasaude.dto.OrderResponseDto;

import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.service.OrderItemService;
import com.oktech.boasaude.service.OrderService;

/**
 * Controller for managing orders in the application.
 * This controller handles HTTP requests related to orders, such as creating,
 * updating, and retrieving orders.
 * 
 * @author João Martins
 * @version 1.0
 */


@RestController
@RequestMapping("/v1/orders")

public class OrderController {
    private final OrderService orderService;

    private final OrderItemService orderItemService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PostMapping()
    public ResponseEntity<OrderResponseDto> createOrder(
        @Valid @RequestBody @NotEmpty List<CreateOrderItemDto> createOrderDto, 
        Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();
        OrderResponseDto order = orderService.createOrder(currentUser, createOrderDto);
        
        logger.info("Order created successfully for user: {}", currentUser.getId());
        
        for (CreateOrderItemDto item : createOrderDto) {
            logger.info("Order item added: Product ID: {}, Quantity: {}", item.productId(), item.quantity());
        }

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
        @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable,
        Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();

        Page<OrderResponseDto> response = orderService.getOrdersByUserId(pageable, currentUser);

        logger.info("Retrieved {} orders for user: {}", response.getTotalElements(), currentUser.getId());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/buy/{orderId}/{status}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
        @PathVariable UUID orderId, 
        @PathVariable String status, 
        Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();


        if (status == null || status.isEmpty()) {
            logger.warn("Status is required for updating order: {}", orderId);
            return ResponseEntity.badRequest().build();
        }

        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, status, currentUser);
        
        logger.info("Order status updated successfully for order ID: {}", orderId);
        
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @GetMapping("/items/sold")
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

}
