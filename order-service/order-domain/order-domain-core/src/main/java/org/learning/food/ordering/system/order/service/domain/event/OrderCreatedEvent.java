package org.learning.food.ordering.system.order.service.domain.event;

import org.learning.food.ordering.system.order.service.domain.entity.Order;

import java.time.OffsetDateTime;

public class OrderCreatedEvent extends OrderEvent {
    public OrderCreatedEvent(Order order, OffsetDateTime createdAt) {
        super(order, createdAt);
    }
}
