package org.learning.food.ordering.system.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.learning.food.ordering.system.domain.valueobject.OrderApprovalStatus;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public record RestaurantApprovalResponse(String id, String sagaId, String orderId, String restaurantId,
                                         Instant createdAt, OrderApprovalStatus orderApprovalStatus,
                                         List<String> failureMessages) {
}
