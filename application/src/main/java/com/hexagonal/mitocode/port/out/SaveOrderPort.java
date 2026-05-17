package com.hexagonal.mitocode.port.out;

import com.hexagonal.mitocode.model.entity.Order;

/**
 * Interfaz que define el puerto de salida para guardar una orden.
 */
public interface SaveOrderPort {

    Order save(Order order);
}
