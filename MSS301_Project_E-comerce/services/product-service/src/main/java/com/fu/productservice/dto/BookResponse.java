package com.fu.productservice.dto;

import java.math.BigDecimal;

public record BookResponse(
        Long id,
        String title,
        String author,
        String category,
        BigDecimal price,
        int stock
) {
}
