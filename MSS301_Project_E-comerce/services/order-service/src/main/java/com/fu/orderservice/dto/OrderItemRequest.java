package com.fu.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        @NotNull(message = "Thiếu mã sách") Long productId,
        @NotNull(message = "Thiếu số lượng") @Positive(message = "Số lượng phải lớn hơn 0") Integer quantity
) {
}
