package com.fu.userservice.config;

import com.fu.userservice.model.Role;
import com.fu.userservice.model.UserAccount;
import com.fu.userservice.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserAccountRepository userAccountRepository) {
        return args -> {
            if (userAccountRepository.count() > 0) {
                return;
            }
            userAccountRepository.save(user("Nguyễn Minh An", "an@bookmart.local", "0901000001", "Hà Nội", Role.CUSTOMER));
            userAccountRepository.save(user("Lê Thu Hà", "ha.staff@bookmart.local", "0901000002", "Hà Nội", Role.STAFF));
        };
    }

    private UserAccount user(String fullName, String email, String phone, String address, Role role) {
        UserAccount user = new UserAccount();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(role);
        return user;
    }

}
