package com.hexagonal.mitocode.service;

import com.hexagonal.mitocode.exception.OrderAlreadyCancelledException;
import com.hexagonal.mitocode.exception.OrderDomainException;
import com.hexagonal.mitocode.exception.OrderNotFoundException;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.model.entity.OrderItem;
import com.hexagonal.mitocode.model.enums.OrderStatus;
import com.hexagonal.mitocode.model.vo.Money;
import com.hexagonal.mitocode.model.vo.OrderId;
import com.hexagonal.mitocode.port.out.FindOrderByIdPort;
import com.hexagonal.mitocode.port.out.NotificationService;
import com.hexagonal.mitocode.port.out.SaveOrderPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CancelOrderServiceTest {

    @Mock
    private SaveOrderPort saveOrderPort;

    @Mock
    private FindOrderByIdPort findOrderByIdPort;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CancelOrderService cancelOrderService;

    @Test
    void should_cancel_pending_order_successfully() {
        // Arrange
        Order order = Order.create("customer-1");
        String orderId = order.getId().toString();
        when(findOrderByIdPort.findById(OrderId.of(orderId))).thenReturn(Optional.of(order));

        // Act
        cancelOrderService.cancelOrder(orderId);

        // Assert
        Assertions.assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(saveOrderPort).save(order);

        verify(notificationService).notifyOrderStatusChange(orderId, order.getStatus());
    }

    @Test
    void should_throw_when_order_not_found() {
        // Arrange
        String orderId = OrderId.generate().toString();
        when(findOrderByIdPort.findById(OrderId.of(orderId))).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(OrderNotFoundException.class,
                () -> cancelOrderService.cancelOrder(orderId));
        verify(saveOrderPort, never()).save(any());
        verify(notificationService, never()).notifyOrderStatusChange(any(), any());
    }

    @Test
    void should_throw_when_cancelling_paid_order() {
        // Arrange
        Order order = Order.create("customer-1");
        order.addItem(new OrderItem("prod-1", "Product", 1,
                Money.of(new BigDecimal("50.00"), "USD")));
        order.pay();
        String orderId = order.getId().toString();
        when(findOrderByIdPort.findById(OrderId.of(orderId))).thenReturn(Optional.of(order));

        // Act & Assert
        Assertions.assertThrows(OrderDomainException.class,
                () -> cancelOrderService.cancelOrder(orderId));
    }

    @Test
    void should_throw_when_cancelling_already_cancelled_order() {
        // Arrange
        Order order = Order.create("customer-1");
        order.cancel();
        String orderId = order.getId().toString();
        when(findOrderByIdPort.findById(OrderId.of(orderId))).thenReturn(Optional.of(order));

        // Act & Assert
        Assertions.assertThrows(OrderAlreadyCancelledException.class,
                () -> cancelOrderService.cancelOrder(orderId));
    }

    @Test
    void should_save_cancelled_order() {
        // Arrange
        Order order = Order.create("customer-1");
        String orderId = order.getId().toString();
        when(findOrderByIdPort.findById(OrderId.of(orderId))).thenReturn(Optional.of(order));

        // Act
        cancelOrderService.cancelOrder(orderId);

        // Assert
        verify(saveOrderPort, times(1)).save(argThat(o ->
                OrderStatus.CANCELLED == o.getStatus()
        ));
    }
}