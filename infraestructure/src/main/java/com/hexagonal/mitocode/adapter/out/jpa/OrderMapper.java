package com.hexagonal.mitocode.adapter.out.jpa;

import com.hexagonal.mitocode.adapter.out.jpa.entity.OrderItemJpaEntity;
import com.hexagonal.mitocode.adapter.out.jpa.entity.OrderJpaEntity;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.model.entity.OrderItem;
import com.hexagonal.mitocode.model.vo.Money;
import com.hexagonal.mitocode.model.vo.OrderId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Mapper para convertir entre OrderJpaEntity y Order (dominio).
 */
@Component
public class OrderMapper {

    // ── Dominio → JPA ─────────────────────────────────────────────

    public OrderJpaEntity toJpa(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();  // usa setter-injection
        entity.setId(order.getId().toString());
        entity.setCustomerId(order.getCustomerId());
        entity.setStatus(order.getStatus());
        entity.setCreatedAt(order.getCreatedAt());
        if (order.getTotal() != null) {
            entity.setTotalAmount(order.getTotal().amount());
            entity.setTotalCurrency(order.getTotal().currency());
        }
        List<OrderItemJpaEntity> itemEntities = order.getItems().stream()
                .map(i -> toItemJpa(i, entity))
                .toList();
        entity.setItems(itemEntities);
        return entity;
    }

    private OrderItemJpaEntity toItemJpa(OrderItem item, OrderJpaEntity parent) {
        OrderItemJpaEntity e = new OrderItemJpaEntity(); // usa setters
        e.setOrder(parent);
        e.setProductId(item.getProductId());
        e.setProductName(item.getProductName());
        e.setQuantity(item.getQuantity());
        e.setUnitPrice(item.getUnitPrice().amount());
        e.setCurrency(item.getUnitPrice().currency());
        return e;
    }

    // ── JPA → Dominio ─────────────────────────────────────────────

    public Order toDomain(OrderJpaEntity entity) {
        Money total = (entity.getTotalAmount() != null && entity.getTotalCurrency() != null)
                ? Money.of(entity.getTotalAmount(), entity.getTotalCurrency())
                : null;
        List<OrderItem> items = entity.getItems().stream()
                .map(this::toItemDomain)
                .toList();
        return Order.reconstitute(
                OrderId.of(entity.getId()),
                entity.getCustomerId(),
                entity.getStatus(),
                total,
                entity.getCreatedAt(),
                items);
    }

    private OrderItem toItemDomain(OrderItemJpaEntity e) {
        return new OrderItem(
                e.getProductId(),
                e.getProductName(),
                e.getQuantity(),
                Money.of(e.getUnitPrice(), e.getCurrency()));
    }
}
