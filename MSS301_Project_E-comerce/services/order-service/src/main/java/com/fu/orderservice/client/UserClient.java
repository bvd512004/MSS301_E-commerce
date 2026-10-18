package com.fu.orderservice.client;

import com.fu.orderservice.exception.BadRequestException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@LoadBalanced RestClient.Builder loadBalancedRestClientBuilder) {
        this.restClient = loadBalancedRestClientBuilder.build();
    }

    public UserView getUser(Long userId) {
        try {
            UserView user = restClient.get()
                    .uri("http://user-service/api/users/{id}", userId)
                    .retrieve()
                    .body(UserView.class);
            if (user == null || user.id() == null) {
                throw new BadRequestException("Khách hàng không tồn tại");
            }
            return user;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new BadRequestException("Khách hàng không tồn tại");
            }
            throw new BadRequestException(readMessage(ex, "Không kiểm tra được khách hàng"));
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
