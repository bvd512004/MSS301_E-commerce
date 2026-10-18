package com.fu.userservice.service;

import com.fu.userservice.dto.UserRequest;
import com.fu.userservice.dto.UserResponse;
import com.fu.userservice.exception.BadRequestException;
import com.fu.userservice.exception.NotFoundException;
import com.fu.userservice.model.Role;
import com.fu.userservice.model.UserAccount;
import com.fu.userservice.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final UserAccountRepository userAccountRepository;

    public AccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userAccountRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public UserResponse register(UserRequest request) {
        String email = normalizeEmail(request.email());
        if (userAccountRepository.existsByEmail(email)) {
            throw new BadRequestException("Email đã được đăng ký");
        }
        UserAccount user = new UserAccount();
        apply(user, request, email);
        return toResponse(userAccountRepository.save(user));
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        UserAccount user = getOrThrow(id);
        String email = normalizeEmail(request.email());
        if (userAccountRepository.existsByEmailAndIdNot(email, id)) {
            throw new BadRequestException("Email đã được đăng ký");
        }
        apply(user, request, email);
        return toResponse(userAccountRepository.save(user));
    }

    private UserAccount getOrThrow(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng " + id));
    }

    private void apply(UserAccount user, UserRequest request, String email) {
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPhone(blankToNull(request.phone()));
        user.setAddress(blankToNull(request.address()));
        user.setRole(request.role() == null ? Role.CUSTOMER : request.role());
    }

    private UserResponse toResponse(UserAccount user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

}
