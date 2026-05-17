package com.hexagonal.mitocode.adapter.in.rest.dto;

import com.hexagonal.mitocode.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/*
 * Response DTO para devolver los datos de la orden.
 */
public record OrderResponse(
        String id,
        String customerId,
        OrderStatus status,
        BigDecimal totalAmount,
        String totalCurrency,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) { }
