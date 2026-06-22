package com.wip.asset_management_system.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {

    private String message;
    private LocalDateTime timestamp;
}
