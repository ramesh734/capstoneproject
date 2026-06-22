package com.wip.asset_management_system.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String username;
    private String password;
    private String role;
}