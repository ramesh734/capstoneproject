package com.wip.asset_management_system.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VendorDTO {

    private Long vendorId;

    @NotBlank
    @Size(min = 3, max = 50, message = "Vendor name must be between 3 and 50 characters")
    private String vendorName;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact must be 10 digits")
    private String contactNumber;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank
    @Size(min = 3, max = 50, message = "Enter Correct Address")
    private String address;
}