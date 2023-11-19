package org.learning.food.ordering.system.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.learning.food.ordering.system.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public record PaymentResponse(String id, String sagaId, String orderId, String paymentId, String customerId,
                              BigDecimal price, Instant createdAt, PaymentStatus paymentStatus,
                              List<String> failureMessages) {
}
