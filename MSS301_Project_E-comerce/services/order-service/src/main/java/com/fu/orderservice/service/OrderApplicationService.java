package com.fu.orderservice.service;

import com.fu.orderservice.client.ProductClient;
import com.fu.orderservice.client.ProductView;
import com.fu.orderservice.client.UserClient;
import com.fu.orderservice.dto.CreateOrderRequest;
import com.fu.orderservice.dto.OrderItemRequest;
import com.fu.orderservice.dto.OrderItemResponse;
import com.fu.orderservice.dto.OrderResponse;
import com.fu.orderservice.exception.BadRequestException;
import com.fu.orderservice.exception.NotFoundException;
import com.fu.orderservice.model.CustomerOrder;
import com.fu.orderservice.model.OrderItem;
import com.fu.orderservice.model.OrderStatus;
import com.fu.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    public OrderApplicationService(
            OrderRepository orderRepository,
            UserClient userClient,
            ProductClient productClient
    ) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.productClient = productClient;
    }

    public List<OrderResponse> findAll(Long userId) {
        List<CustomerOrder> orders = userId == null
                ? orderRepository.findAllWithItems()
                : orderRepository.findByUserIdWithItems(userId);
        return orders.stream().map(this::toResponse).toList();
    }

    public OrderResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public OrderResponse create(CreateOrderRequest request) {
        userClient.getUser(request.userId());
        Map<Long, Integer> quantities = mergeItems(request.items());
        List<ReservedLine> reserved = new ArrayList<>();
        try {
            for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
                ProductView product = productClient.reserve(entry.getKey(), entry.getValue());
                reserved.add(new ReservedLine(entry.getKey(), entry.getValue(), product.price()));
            }
            CustomerOrder order = new CustomerOrder();
            order.setUserId(request.userId());
            order.setStatus(OrderStatus.PENDING);
            order.setCreatedAt(Instant.now());
            for (ReservedLine line : reserved) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(line.productId());
                item.setQuantity(line.quantity());
                item.setUnitPrice(line.unitPrice());
                order.getItems().add(item);
            }
            return toResponse(orderRepository.save(order));
        } catch (RuntimeException ex) {
            releaseQuietly(reserved);
            throw ex;
        }
    }

    public OrderResponse updateStatus(Long id, OrderStatus next) {
        CustomerOrder order = getOrThrow(id);
        if (!allowed(order.getStatus(), next)) {
            throw new BadRequestException(
                    "Không chuyển được từ " + order.getStatus() + " sang " + next
            );
        }
        if (next == OrderStatus.CANCELLED) {
            releaseForCancel(order);
        }
        order.setStatus(next);
        return toResponse(orderRepository.save(order));
    }

    private void releaseForCancel(CustomerOrder order) {
        List<OrderItem> released = new ArrayList<>();
        try {
            for (OrderItem item : order.getItems()) {
                productClient.release(item.getProductId(), item.getQuantity());
                released.add(item);
            }
        } catch (RuntimeException ex) {
            for (OrderItem item : released) {
                productClient.reserve(item.getProductId(), item.getQuantity());
            }
            throw ex;
        }
    }

    private void releaseQuietly(List<ReservedLine> reserved) {
        for (ReservedLine line : reserved) {
            try {
                productClient.release(line.productId(), line.quantity());
            } catch (RuntimeException ignored) {
                // Giữ đơn không thành công thì cố hoàn kho đã trừ.
            }
        }
    }

    private boolean allowed(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case PENDING -> next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
            case CONFIRMED -> next == OrderStatus.SHIPPING || next == OrderStatus.CANCELLED;
            case SHIPPING -> next == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    private Map<Long, Integer> mergeItems(List<OrderItemRequest> items) {
        Map<Long, Integer> quantities = new LinkedHashMap<>();
        for (OrderItemRequest item : items) {
            quantities.merge(item.productId(), item.quantity(), Integer::sum);
        }
        return quantities;
    }

    private CustomerOrder getOrThrow(Long id) {
        return orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng " + id));
    }

    private OrderResponse toResponse(CustomerOrder order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(item.getProductId(), item.getQuantity(), item.getUnitPrice()))
                .toList();
        BigDecimal total = items.stream()
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getCreatedAt(),
                items,
                total
        );
    }

    private record ReservedLine(Long productId, int quantity, BigDecimal unitPrice) {
    }

}
