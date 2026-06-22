package com.wip.asset_management_system.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AssetAllocationDTO {

    private Long allocationId;

    @NotNull(message = "Asset ID is required")
    private Long assetId;

    private String assetName;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    @NotNull(message = "Allocated date required")
    private LocalDate allocatedDate;

    private LocalDate returnDate;

    @NotBlank
    private String status;
    @NotBlank
    @Size(min = 3, max = 50, message = "Purpose should not be blank")
    private String remarks;
}
