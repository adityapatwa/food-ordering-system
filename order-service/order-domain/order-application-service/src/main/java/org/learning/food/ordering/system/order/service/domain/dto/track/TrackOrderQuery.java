package org.learning.food.ordering.system.order.service.domain.dto.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.learning.food.ordering.system.domain.valueobject.OrderStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public record TrackOrderQuery(@NotNull UUID orderTrackingId, @NotNull OrderStatus orderStatus,
                              List<String> failureMessages) {
}
