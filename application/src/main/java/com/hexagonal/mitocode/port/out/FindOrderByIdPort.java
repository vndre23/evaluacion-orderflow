package com.hexagonal.mitocode.port.out;

import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.model.vo.OrderId;

import java.util.Optional;

/**
 * Interfaz que define el puerto de salida para encontrar una orden por su ID
 */
public interface FindOrderByIdPort {

    Optional<Order> findById(OrderId orderId);
}
