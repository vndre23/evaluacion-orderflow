package com.hexagonal.mitocode.port.in;

import com.hexagonal.mitocode.command.AddItemToOrderCommand;
import com.hexagonal.mitocode.model.entity.Order;

/**
 * Interfaz que define el caso de uso para agregar un item a una orden existente.
 */
public interface AddItemToOrderUseCase {
    Order addItem(AddItemToOrderCommand command);
}
