package com.fu.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemResponse(Long productId, int quantity, BigDecimal unitPrice) {
}
