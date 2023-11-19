package org.learning.food.ordering.system.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public record OrderAddress(@NotNull @Max(50) String street, @NotNull @Max(10) String postalCode,
                           @NotNull @Max(50) String city) {
}
