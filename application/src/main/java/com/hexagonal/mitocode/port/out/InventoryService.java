package com.hexagonal.mitocode.port.out;

/**
 * Interfaz que define el puerto de salida para consultar la disponibilidad de stock del producto
 */
public interface InventoryService {

    boolean isAvailable(String productId, int quantity);
}
