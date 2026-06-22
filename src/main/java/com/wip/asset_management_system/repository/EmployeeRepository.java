package com.wip.asset_management_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wip.asset_management_system.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}