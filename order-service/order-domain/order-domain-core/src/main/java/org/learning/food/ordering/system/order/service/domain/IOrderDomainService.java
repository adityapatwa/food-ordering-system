package org.learning.food.ordering.system.order.service.domain;

import org.learning.food.ordering.system.order.service.domain.entity.Order;
import org.learning.food.ordering.system.order.service.domain.entity.Restaurant;
import org.learning.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import org.learning.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import org.learning.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface IOrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
