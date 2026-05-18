package com.hexagonal.mitocode.service;

import com.hexagonal.mitocode.exception.OrderNotFoundException;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.model.entity.OrderItem;
import com.hexagonal.mitocode.model.enums.OrderStatus;
import com.hexagonal.mitocode.model.vo.Money;
import com.hexagonal.mitocode.model.vo.OrderId;
import com.hexagonal.mitocode.port.out.FindOrderByIdPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class GetOrderByIdServiceTest {

    @Mock
    private FindOrderByIdPort findOrderByIdPort;

    @InjectMocks
    private GetOrderByIdService getOrderByIdService;

    @Test
    void should_get_order_successfully() {
        // Arrange
        Order order = Order.create("customer-1");
        String orderId = order.getId().toString();

        when(findOrderByIdPort.findById(OrderId.of(orderId))).thenReturn(Optional.of(order));


        // Act
        getOrderByIdService.getOrderById(orderId);

        // Assert
        Assertions.assertEquals(OrderStatus.PENDING, order.getStatus());
        verify(findOrderByIdPort).findById(order.getId());
    }


    @Test
    void should_throw_when_get_order_not_found() {
        // Arrange
        String orderId = OrderId.generate().toString();
        when(findOrderByIdPort.findById(any(OrderId.class))).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(OrderNotFoundException.class,
                () -> getOrderByIdService.getOrderById(orderId));
        verify(findOrderByIdPort).findById(any());
    }
}
