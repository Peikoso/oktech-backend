package com.oktech.boasaude.entity;


/**
 * Enumeração que representa os possíveis status de um pedido.
 * 
 * @author João Martins
 * @version 1.0
 */

public enum OrderDeliveryStatus {
    CANCELLED("CANCELLED"),
    PENDING("PENDING"),
    SHIPPED("SHIPPED"),
    DELIVERED("DELIVERED");

    private final String status;

    OrderDeliveryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
