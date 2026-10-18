package com.fu.orderservice.client;

import com.fu.orderservice.exception.BadRequestException;
import com.fu.orderservice.exception.NotFoundException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(@LoadBalanced RestClient.Builder loadBalancedRestClientBuilder) {
        this.restClient = loadBalancedRestClientBuilder.build();
    }

    public ProductView reserve(Long productId, int quantity) {
        return post("/api/products/{id}/reserve", productId, quantity, "Không giữ được tồn kho");
    }

    public ProductView release(Long productId, int quantity) {
        return post("/api/products/{id}/release", productId, quantity, "Không hoàn được tồn kho");
    }

    private ProductView post(String path, Long productId, int quantity, String fallback) {
        try {
            ProductView product = restClient.post()
                    .uri("http://product-service" + path, productId)
                    .body(new QuantityBody(quantity))
                    .retrieve()
                    .body(ProductView.class);
            if (product == null || product.price() == null) {
                throw new BadRequestException(fallback);
            }
            return product;
        } catch (RestClientResponseException ex) {
            String message = readMessage(ex, fallback);
            if (ex.getStatusCode().value() == 404) {
                throw new NotFoundException(message);
            }
            throw new BadRequestException(message);
        }
    }

    private String readMessage(RestClientResponseException ex, String fallback) {
        try {
            RemoteError error = ex.getResponseBodyAs(RemoteError.class);
            if (error != null && error.message() != null && !error.message().isBlank()) {
                return error.message();
            }
        } catch (Exception ignored) {
            // body is not the expected JSON
        }
        return fallback;
    }

}
