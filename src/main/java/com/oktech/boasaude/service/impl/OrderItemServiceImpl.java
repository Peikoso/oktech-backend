package com.oktech.boasaude.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.oktech.boasaude.dto.OrderItemResponseDto;
import com.oktech.boasaude.dto.ShopResponseDto;
import com.oktech.boasaude.entity.Address;
import com.oktech.boasaude.entity.Order;
import com.oktech.boasaude.entity.OrderDeliveryStatus;
import com.oktech.boasaude.entity.OrderItem;
import com.oktech.boasaude.entity.OrderStatus;
import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.repository.OrderItemRepository;
import com.oktech.boasaude.service.AddressService;
import com.oktech.boasaude.service.OrderItemService;
import com.oktech.boasaude.service.ProductService;
import com.oktech.boasaude.service.ShopService;

/**
 * Implementação do serviço de itens de pedido.
 * Fornece métodos para adicionar, atualizar e excluir itens de pedido.
 * @author João Martins
 * @version 1.0
 */

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    
    private final ProductService productService;

    private final ShopService shopService;

    private final AddressService addressService;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ProductService productService, ShopService shopService, AddressService addressService) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.shopService = shopService;
        this.addressService = addressService;
    }

    /**
     * Adiciona um item de pedido ao pedido especificado.
     * @param order O pedido ao qual o item será adicionado.
     * @param productId O ID do produto a ser adicionado.
     * @param quantity A quantidade do produto a ser adicionada.
     * @return O item de pedido criado e salvo.
     */
    @Override
    public OrderItem addOrderItem(Order order, UUID productId, int quantity, UUID addressId, User currentUser) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Product product = productService.getProductById(productId);

        Address address = addressService.getAddressEntityById(addressId, currentUser);

        OrderItem orderItem = new OrderItem(order, product, quantity, address);

        return orderItemRepository.save(orderItem);
    }

    /**
     * Obtém os itens de pedido associados a um pedido específico.
     * @param orderId O ID do pedido cujos itens serão recuperados.
     * @return A lista de itens de pedido associados ao pedido.
     */
    @Override
    public List<OrderItem> getOrderItemsByOrderId(UUID orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        return orderItems;
    }


    @Override
    public List<OrderItemResponseDto> getSoldItems(User user) {
        ShopResponseDto shop = shopService.getShopbyuser(user);
            
        List<OrderItem> items = orderItemRepository.findByOrder_StatusAndProduct_Shop_IdOrderByCreatedAtDesc(OrderStatus.COMPLETED, shop.id());

        return items.stream()
                .map(OrderItemResponseDto::new)
                .toList();  
    }

    @Override
    public OrderItemResponseDto updateDeliveryStatus(UUID orderItemId, User currentUser, String status) {

        OrderItem item = orderItemRepository.findByIdAndProduct_Shop_Owner_Id(orderItemId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order item not found"));

        OrderDeliveryStatus statusEnum;
        try {
            statusEnum = OrderDeliveryStatus.valueOf(status.toUpperCase()); // Usa toUpperCase para garantir que combine
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
        item.setDeliveryStatus(statusEnum);
        orderItemRepository.save(item);

        return new OrderItemResponseDto(item);
    }

}
