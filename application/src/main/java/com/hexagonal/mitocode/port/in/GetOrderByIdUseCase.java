package com.hexagonal.mitocode.port.in;

import com.hexagonal.mitocode.model.entity.Order;

import java.util.Optional;

public interface GetOrderByIdUseCase {

    Order getOrderById(String id);
}
