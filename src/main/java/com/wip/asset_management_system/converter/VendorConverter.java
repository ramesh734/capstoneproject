package com.wip.asset_management_system.converter;

import com.wip.asset_management_system.dto.VendorDTO;
import com.wip.asset_management_system.entity.Vendor;

public class VendorConverter {

    public static VendorDTO toDTO(Vendor v) {
        VendorDTO dto = new VendorDTO();
        dto.setVendorId(v.getVendorId());
        dto.setVendorName(v.getVendorName());
        dto.setContactNumber(v.getContactNumber());
        dto.setEmail(v.getEmail());
        dto.setAddress(v.getAddress());
        return dto;
    }
}