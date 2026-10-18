package com.fu.userservice.dto;

import com.fu.userservice.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Họ tên không được để trống") String fullName,
        @NotBlank(message = "Email không được để trống") @Email(message = "Email không hợp lệ") String email,
        String phone,
        String address,
        Role role
) {
}
