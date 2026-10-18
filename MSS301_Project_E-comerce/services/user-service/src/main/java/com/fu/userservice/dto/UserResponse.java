package com.fu.userservice.dto;

import com.fu.userservice.model.Role;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String address,
        Role role
) {
}
