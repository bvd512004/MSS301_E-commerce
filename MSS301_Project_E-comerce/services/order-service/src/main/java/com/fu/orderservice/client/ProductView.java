package com.fu.orderservice.client;

import java.math.BigDecimal;

public record ProductView(Long id, String title, BigDecimal price, int stock) {
}
