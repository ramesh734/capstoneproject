package com.wip.asset_management_system.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wip.asset_management_system.entity.Employee;
import com.wip.asset_management_system.repository.EmployeeRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(EmployeeRepository repo, PasswordEncoder encoder) {
        return args -> {

            // create admin only if not exists
            if (repo.findByUsername("admin").isEmpty()) {

                Employee admin = new Employee();
                admin.setEmployeeName("Admin");
                admin.setEmail("admin@gmail.com");
                admin.setDepartment("IT");
                admin.setUsername("admin");

                // IMPORTANT: encrypted password
                admin.setPassword(encoder.encode("admin123"));

                admin.setRole("ROLE_ADMIN");

                repo.save(admin);
            }
        };
    }
}
