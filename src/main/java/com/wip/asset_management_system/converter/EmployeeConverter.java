package com.wip.asset_management_system.converter;

import com.wip.asset_management_system.dto.EmployeeDTO;
import com.wip.asset_management_system.entity.Employee;

public class EmployeeConverter {

    public static EmployeeDTO toDTO(Employee employee) {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setEmployeeId(employee.getEmployeeId());
        dto.setEmployeeName(employee.getEmployeeName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());
        dto.setUsername(employee.getUsername());
        dto.setPassword(employee.getPassword());
        dto.setRole(employee.getRole());

        return dto;
    }
}