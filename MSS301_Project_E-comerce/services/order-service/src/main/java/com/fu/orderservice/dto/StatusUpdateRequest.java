package com.fu.orderservice.dto;

import com.fu.orderservice.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull(message = "Thiếu trạng thái") OrderStatus status
) {
}
