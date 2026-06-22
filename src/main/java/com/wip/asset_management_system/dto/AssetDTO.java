package com.wip.asset_management_system.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AssetDTO {

    private Long assetId;

    @NotBlank
    @Size(min = 3, max = 50, message = "AssetName must be between 3 and 50 characters")
    private String assetName;

    @NotBlank
    @Size(min = 3, max = 50, message = "Serial number must be between 3 and 50 characters")
    private String serialNumber;

    @NotBlank
    @Size(min = 3, max = 50, message = "Enter availability correctly")
    private String availability;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private String categoryName;

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    private String vendorName;
}
