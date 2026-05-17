package com.hexagonal.mitocode.adapter.out.inventory;


import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class SimulatedInventoryServiceTest {

    private final SimulatedInventoryService service = new SimulatedInventoryService();

    @Test
    void isAvailable_shouldAlwaysReturnTrue() {
        assertTrue(service.isAvailable("PROD-1", 10));
    }

    @Test
    void isAvailable_shouldReturnTrueForZeroQuantity() {
        assertTrue(service.isAvailable("PROD-2", 0));
    }

    @Test
    void isAvailable_shouldReturnTrueForAnyProduct() {
        assertTrue(service.isAvailable("ANY-PRODUCT", 999));
    }
}