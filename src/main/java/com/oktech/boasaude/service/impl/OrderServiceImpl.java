package com.oktech.boasaude.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oktech.boasaude.entity.Order;
import com.oktech.boasaude.entity.OrderItem;
import com.oktech.boasaude.entity.OrderStatus;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.repository.OrderRepository;
import com.oktech.boasaude.service.OrderService;
import com.oktech.boasaude.dto.CreateOrderItemDto;
import com.oktech.boasaude.dto.OrderResponseDto;

/**
 * Implementação do serviço de pedidos.
 * Fornece métodos para criar, atualizar, obter e excluir pedidos.
 * @author João Martins
 * @version 1.0
 * @author Lucas Ouro
 * @version 1.1
 * Mudança do metodo de listagem de pedidos trazendo pedidos por usuário
 * e ordenando por data de criação mais recente.
 * @see OrderService
 */

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemServiceImpl orderItemServiceImpl;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemServiceImpl orderItemServiceImpl) {
        this.orderRepository = orderRepository;
        this.orderItemServiceImpl = orderItemServiceImpl;
    }

    /**
     * Cria um novo pedido para o usuário atual com os itens de pedido fornecidos.
     * @param currentUser O usuário atual que está criando o pedido.
     * @param orderItems A lista de itens de pedido a serem adicionados ao pedido.
     * @return O pedido criado com os itens associados.
    */
    @Override
    public OrderResponseDto createOrder(User currentUser, UUID addressId, List<CreateOrderItemDto> orderItems) {
        Order order = new Order(currentUser);

        orderRepository.save(order);
        
        for (CreateOrderItemDto itemDto : orderItems) {
            OrderItem orderItem = orderItemServiceImpl.addOrderItem(order, itemDto.productId(), itemDto.quantity(), addressId, currentUser);
            order.getItems().add(orderItem);
        }

        order = orderRepository.save(order);
        
        return new OrderResponseDto(order);
    }

    @Override
    public OrderResponseDto updateOrderStatus(UUID orderId, String status, User currentUser) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this order.");
        }

        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        OrderStatus statusEnum;
        try {
            statusEnum = OrderStatus.valueOf(status.toUpperCase()); // Usa toUpperCase para garantir que combine
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

        order.updateStatus(statusEnum);
        order = orderRepository.save(order);

        return new OrderResponseDto(order); // Retorna a resposta com os dados do pedido atualizado
    }

    @Override
    public OrderResponseDto getOrderById(UUID orderId, User currentUser) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You do not have permission to view this order.");
        }

        return new OrderResponseDto(order); // Retorna a resposta com os dados do pedido
    }

    @Override
    public Page<OrderResponseDto> getOrdersByUserId(Pageable pageable, User currentUser) {
        Page<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId(), pageable);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("No orders found for user with ID: " + currentUser.getId());
        }
        Page<OrderResponseDto> response = orders.map(OrderResponseDto::new);

        return response; // Retorna a página de pedidos do usuário
    }

    @Override
    public void deleteOrder(UUID orderId, User currentUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOrder'");
    }
    
}
