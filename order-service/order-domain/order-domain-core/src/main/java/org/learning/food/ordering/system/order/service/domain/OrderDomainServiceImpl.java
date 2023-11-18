package org.learning.food.ordering.system.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.learning.food.ordering.system.domain.valueobject.ProductId;
import org.learning.food.ordering.system.order.service.domain.entity.Order;
import org.learning.food.ordering.system.order.service.domain.entity.Product;
import org.learning.food.ordering.system.order.service.domain.entity.Restaurant;
import org.learning.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import org.learning.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import org.learning.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import org.learning.food.ordering.system.order.service.domain.exception.OrderDomainException;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class OrderDomainServiceImpl implements IOrderDomainService {
    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, OffsetDateTime.now(ZoneId.of(UTC)));

    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, OffsetDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is paid", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is being cancelled for order with id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, OffsetDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() + " is currently not active!");
        }
    }


    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        Map<ProductId, Product> restaurantProductMap = restaurant.getProducts().stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = restaurantProductMap.get(currentProduct.getId());
            currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
        });
    }
}
