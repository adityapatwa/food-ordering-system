package org.learning.food.ordering.system.order.service.domain.entity;

import org.learning.food.ordering.system.domain.entity.AggregateRoot;
import org.learning.food.ordering.system.domain.valueobject.*;
import org.learning.food.ordering.system.order.service.domain.exception.OrderDomainException;
import org.learning.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import org.learning.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.learning.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.Objects;

import static java.util.UUID.randomUUID;
import static org.learning.food.ordering.system.domain.valueobject.Money.ZERO;
import static org.learning.food.ordering.system.domain.valueobject.OrderStatus.*;

public class Order extends AggregateRoot<OrderId> {
    public static final String PAY_OPERATION_EXCEPTION_MESSAGE = "Order is not in the correct state for pay operation!";
    public static final String APPROVE_OPERATION_EXCEPTION_MESSAGE = "Order is not in the correct state for approve operation!";
    public static final String INIT_CANCEL_OPERATION_EXCEPTION_MESSAGE = "Order is not in the correct state for initCancel operation!";
    public static final String CANCEL_OPERATION_EXCEPTION_MESSAGE = "Order is not in the correct state for cancel operation!";
    public static final String ORDER_INITIALIZATION_EXCEPTION_MESSAGE = "Order is not in correct state for initialization!";
    public static final String INVALID_ORDER_PRICE_EXCEPTION_MESSAGE = "Total price must be greater than zero!";
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        streetAddress = builder.streetAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (!orderStatus.equals(PENDING)) {
            throw new OrderDomainException(PAY_OPERATION_EXCEPTION_MESSAGE);
        }
        orderStatus = PAID;
    }

    public void approve() {
        if (!orderStatus.equals(PAID)) {
            throw new OrderDomainException(APPROVE_OPERATION_EXCEPTION_MESSAGE);
        }
        orderStatus = APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (!orderStatus.equals(PAID)) {
            throw new OrderDomainException(INIT_CANCEL_OPERATION_EXCEPTION_MESSAGE);
        }
        orderStatus = CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {
        if (!(orderStatus.equals(PENDING) || orderStatus.equals(CANCELLING))) {
            throw new OrderDomainException(CANCEL_OPERATION_EXCEPTION_MESSAGE);
        }
        orderStatus = CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        assert !Objects.isNull(failureMessages);

        if (!Objects.isNull(this.failureMessages)) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isBlank()).toList());
        }

        if (Objects.isNull(this.failureMessages)) {
            this.failureMessages = failureMessages.stream().filter(message -> !message.isBlank()).toList();
        }
    }

    private void validateInitialOrder() {
        if (!(Objects.isNull(orderStatus) || Objects.isNull(getId()))) {
            throw new OrderDomainException(ORDER_INITIALIZATION_EXCEPTION_MESSAGE);
        }
    }

    private void validateTotalPrice() {
        if (Objects.isNull(price) || !price.isGreaterThanZero()) {
            throw new OrderDomainException(INVALID_ORDER_PRICE_EXCEPTION_MESSAGE);
        }
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(ZERO, Money::add);

        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.amount() + "is not equal to Order items total: " +
                    orderItemsTotal.amount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().amount() +
                    "is not valid for product " + orderItem.getProduct().getId().getValue() + "!");
        }
    }

    public void initializeOrder() {
        setId(new OrderId(randomUUID()));
        trackingId = new TrackingId(randomUUID());
        orderStatus = PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress streetAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder streetAddress(StreetAddress val) {
            streetAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
