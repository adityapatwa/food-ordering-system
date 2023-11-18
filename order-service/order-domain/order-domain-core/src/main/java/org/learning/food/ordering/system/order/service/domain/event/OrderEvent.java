package org.learning.food.ordering.system.order.service.domain.event;

import org.learning.food.ordering.system.domain.event.DomainEvent;
import org.learning.food.ordering.system.order.service.domain.entity.Order;

import java.time.OffsetDateTime;

public abstract class OrderEvent implements DomainEvent<Order> {
    private final Order order;
    private final OffsetDateTime createdAt;

    public OrderEvent(Order order, OffsetDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
