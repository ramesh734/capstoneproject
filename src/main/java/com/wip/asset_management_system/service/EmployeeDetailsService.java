package com.wip.asset_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wip.asset_management_system.entity.Employee;
import com.wip.asset_management_system.repository.EmployeeRepository;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 🔍 Fetch user from DB
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 🔐 Return Spring Security User object
        return new User(
                employee.getUsername(),
                employee.getPassword(), // must be BCrypt encoded
                List.of(new SimpleGrantedAuthority(employee.getRole())) // ROLE_ADMIN / ROLE_EMPLOYEE
        );
    }
}