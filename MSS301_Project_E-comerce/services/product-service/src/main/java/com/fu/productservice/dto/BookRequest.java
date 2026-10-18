package com.fu.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BookRequest(
        @NotBlank(message = "Tên sách không được để trống") String title,
        @NotBlank(message = "Tác giả không được để trống") String author,
        @NotBlank(message = "Danh mục không được để trống") String category,
        @NotNull(message = "Giá không được để trống") @DecimalMin(value = "0.0", message = "Giá không được âm") BigDecimal price,
        @NotNull(message = "Tồn kho không được để trống") @Min(value = 0, message = "Tồn kho không được âm") Integer stock
) {
}
