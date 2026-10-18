package com.fu.productservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QuantityRequest(
        @NotNull(message = "Thiếu số lượng") @Positive(message = "Số lượng phải lớn hơn 0") Integer quantity
) {
}
