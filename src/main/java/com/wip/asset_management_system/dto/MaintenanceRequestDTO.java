package com.wip.asset_management_system.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceRequestDTO {

    private Long requestId;

    @NotNull(message = "Asset ID required")
    private Long assetId;

    private String assetName;

    @NotBlank(message = "Description required")
    @Size(min = 3, max = 50, message = "Enter Description properly")
    private String description;

    private String status;

    private LocalDateTime createdAt;
    private Long employeeId;
}
