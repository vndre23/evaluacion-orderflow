package com.hexagonal.mitocode.port.in;

import com.hexagonal.mitocode.command.CreateOrderCommand;
import com.hexagonal.mitocode.model.entity.Order;

/**
 * Interfaz que define el caso de uso para crear una nueva orden
 */
public interface CreateOrderUseCase {

    Order createOrder(CreateOrderCommand command);
}
