package org.learning.food.ordering.system.order.service.domain.event;

import org.learning.food.ordering.system.order.service.domain.entity.Order;

import java.time.OffsetDateTime;

public class OrderPaidEvent extends OrderEvent {
    public OrderPaidEvent(Order order, OffsetDateTime createdAt) {
        super(order, createdAt);
    }
}
