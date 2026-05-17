package com.hexagonal.mitocode.adapter.in.rest;

import com.hexagonal.mitocode.adapter.in.rest.dto.AddItemRequest;
import com.hexagonal.mitocode.adapter.in.rest.dto.OrderResponse;
import com.hexagonal.mitocode.adapter.in.rest.dto.OrderResponseMapper;
import com.hexagonal.mitocode.adapter.in.rest.dto.CreateOrderRequest;
import com.hexagonal.mitocode.command.AddItemToOrderCommand;
import com.hexagonal.mitocode.command.CreateOrderCommand;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.model.entity.OrderItem;
import com.hexagonal.mitocode.model.enums.OrderStatus;
import com.hexagonal.mitocode.model.vo.Money;
import com.hexagonal.mitocode.model.vo.OrderId;
import com.hexagonal.mitocode.port.in.AddItemToOrderUseCase;
import com.hexagonal.mitocode.port.in.CancelOrderUseCase;
import com.hexagonal.mitocode.port.in.CreateOrderUseCase;
import com.hexagonal.mitocode.port.in.PayOrderUseCase;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private AddItemToOrderUseCase addItemUseCase;

    @Mock
    private PayOrderUseCase payOrderUseCase;

    @Mock
    private CancelOrderUseCase cancelOrderUseCase;

    @Mock
    private OrderResponseMapper responseMapper;

    @InjectMocks
    private OrderController controller;

    // ── Helpers ─────────────────────────────────────────────────────

    private Order createDomainOrder() {
        OrderId id = OrderId.of("550e8400-e29b-41d4-a716-446655440000");
        return Order.reconstitute(id, "customer-1", OrderStatus.PENDING,
                null, LocalDateTime.of(2025, 6, 1, 10, 0), List.of());
    }

    private Order createDomainOrderWithItem() {
        OrderId id = OrderId.of("550e8400-e29b-41d4-a716-446655440000");
        Money total = Money.of(new BigDecimal("200.00"), "EUR");
        OrderItem item = new OrderItem("PROD-1", "Laptop", 2,
                Money.of(new BigDecimal("100.00"), "EUR"));
        return Order.reconstitute(id, "customer-1", OrderStatus.PENDING,
                total, LocalDateTime.of(2025, 6, 1, 10, 0), List.of(item));
    }

    private OrderResponse createOrderResponse() {
        return new OrderResponse(
                "550e8400-e29b-41d4-a716-446655440000",
                "customer-1",
                OrderStatus.PENDING,
                null, null,
                LocalDateTime.of(2025, 6, 1, 10, 0),
                List.of()
        );
    }

    private void setupRequestContext() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/orders");
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setScheme("http");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    // ── createOrder ─────────────────────────────────────────────────

    @Test
    void createOrder_shouldReturnCreatedWithLocationHeader() {
        setupRequestContext();
        try {
            Order order = createDomainOrder();
            OrderResponse orderResponse = createOrderResponse();

            when(createOrderUseCase.createOrder(any(CreateOrderCommand.class))).thenReturn(order);
            when(responseMapper.toResponse(order)).thenReturn(orderResponse);

            var request = new CreateOrderRequest("customer-1");
            ResponseEntity<OrderResponse> response = controller.createOrder(request);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getHeaders().getLocation());
            assertTrue(response.getHeaders().getLocation().toString()
                    .contains("550e8400-e29b-41d4-a716-446655440000"));
            assertNotNull(response.getBody());
            assertEquals("customer-1", response.getBody().customerId());
            verify(createOrderUseCase).createOrder(any(CreateOrderCommand.class));
            verify(responseMapper).toResponse(order);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    void createOrder_shouldPassCorrectCommandToUseCase() {
        setupRequestContext();
        try {
            Order order = createDomainOrder();
            when(createOrderUseCase.createOrder(any(CreateOrderCommand.class))).thenReturn(order);
            when(responseMapper.toResponse(any())).thenReturn(createOrderResponse());

            var request = new CreateOrderRequest("customer-99");
            controller.createOrder(request);

            verify(createOrderUseCase).createOrder(argThat(cmd ->
                    "customer-99".equals(cmd.customerId())
            ));
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    // ── addItem ─────────────────────────────────────────────────────

    @Test
    void addItem_shouldReturnOkWithOrderResponse() {
        Order order = createDomainOrderWithItem();
        OrderResponse orderResponse = new OrderResponse(
                "550e8400-e29b-41d4-a716-446655440000",
                "customer-1", OrderStatus.PENDING,
                new BigDecimal("200.00"), "EUR",
                LocalDateTime.of(2025, 6, 1, 10, 0),
                List.of()
        );

        when(addItemUseCase.addItem(any(AddItemToOrderCommand.class))).thenReturn(order);
        when(responseMapper.toResponse(order)).thenReturn(orderResponse);

        var request = new AddItemRequest(
                "PROD-1", "Laptop", 2, new BigDecimal("100.00"), "EUR");
        ResponseEntity<OrderResponse> response =
                controller.addItem("550e8400-e29b-41d4-a716-446655440000", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(addItemUseCase).addItem(any(AddItemToOrderCommand.class));
    }

    @Test
    void addItem_shouldPassCorrectCommandToUseCase() {
        Order order = createDomainOrderWithItem();
        when(addItemUseCase.addItem(any(AddItemToOrderCommand.class))).thenReturn(order);
        when(responseMapper.toResponse(any())).thenReturn(createOrderResponse());

        var request = new AddItemRequest(
                "PROD-1", "Laptop", 2, new BigDecimal("100.00"), "EUR");
        controller.addItem("order-123", request);

        verify(addItemUseCase).addItem(argThat(cmd ->
                "order-123".equals(cmd.orderId())
                        && "PROD-1".equals(cmd.productId())
                        && "Laptop".equals(cmd.productName())
                        && cmd.quantity() == 2
                        && new BigDecimal("100.00").equals(cmd.unitPrice())
                        && "EUR".equals(cmd.currency())
        ));
    }

    // ── payOrder ────────────────────────────────────────────────────

    @Test
    void payOrder_shouldReturnNoContent() {
        doNothing().when(payOrderUseCase).payOrder("order-123");

        ResponseEntity<Void> response = controller.payOrder("order-123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(payOrderUseCase).payOrder("order-123");
    }

    // ── cancelOrder ─────────────────────────────────────────────────

    @Test
    void cancelOrder_shouldReturnNoContent() {
        doNothing().when(cancelOrderUseCase).cancelOrder("order-456");

        ResponseEntity<Void> response = controller.cancelOrder("order-456");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(cancelOrderUseCase).cancelOrder("order-456");
    }
}
