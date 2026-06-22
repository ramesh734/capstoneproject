package com.wip.asset_management_system.converter;

import com.wip.asset_management_system.dto.AssetDTO;
import com.wip.asset_management_system.entity.Asset;

public class AssetConverter {

    public static AssetDTO toDTO(Asset a) {
        AssetDTO dto = new AssetDTO();

        dto.setAssetId(a.getAssetId());
        dto.setAssetName(a.getAssetName());
        dto.setSerialNumber(a.getSerialNumber());
        dto.setAvailability(a.getAvailability());

        if (a.getCategory() != null) {
            dto.setCategoryId(a.getCategory().getCategoryId());
            dto.setCategoryName(a.getCategory().getCategoryName());
        }

        if (a.getVendor() != null) {
            dto.setVendorId(a.getVendor().getVendorId());
            dto.setVendorName(a.getVendor().getVendorName());
        }

        return dto;
    }
}
