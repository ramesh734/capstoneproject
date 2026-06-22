package com.wip.asset_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.wip.asset_management_system.dto.LoginRequestDTO;
import com.wip.asset_management_system.dto.LoginResponseDTO;
import com.wip.asset_management_system.entity.Employee;
import com.wip.asset_management_system.repository.EmployeeRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDTO request) {

        Employee employee =
                employeeRepository
                .findByUsername(request.getUsername())
                .orElse(null);

        if(employee == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid Username");
        }

        if(!passwordEncoder.matches(
                request.getPassword(),
                employee.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid Password");
        }

        if(!employee.getRole()
                .equals(request.getRole())) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid Role Selected");
        }

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        "Login Successful",
                        employee.getRole(),
                        employee.getEmployeeId()
                )
        );
    }
}