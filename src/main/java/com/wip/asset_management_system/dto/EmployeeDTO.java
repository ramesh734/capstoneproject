package com.wip.asset_management_system.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeDTO {

    private Long employeeId;

    @Size(min = 3, max = 50, message = "Employee name must be between 3 and 50 characters")
    @NotBlank(message = "Employee name is required")
    private String employeeName;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Department is required")
    private String department;

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @NotBlank(message = "Username is required")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;
}