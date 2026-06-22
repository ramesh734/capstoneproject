package com.wip.asset_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private String message;
    private String role;
    private Long employeeId;
}