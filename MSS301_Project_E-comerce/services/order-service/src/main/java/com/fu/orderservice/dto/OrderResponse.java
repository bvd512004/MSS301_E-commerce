package com.fu.orderservice.dto;

import com.fu.orderservice.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        OrderStatus status,
        Instant createdAt,
        List<OrderItemResponse> items,
        BigDecimal total
) {
}
