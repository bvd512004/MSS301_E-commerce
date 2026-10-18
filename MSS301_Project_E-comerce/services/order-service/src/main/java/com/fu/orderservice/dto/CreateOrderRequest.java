package com.fu.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "Thiếu mã khách hàng") Long userId,
        @NotEmpty(message = "Đơn hàng phải có ít nhất một sách") List<@Valid OrderItemRequest> items
) {
}
